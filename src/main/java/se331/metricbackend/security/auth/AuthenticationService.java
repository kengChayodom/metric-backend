package se331.metricbackend.security.auth;

// ... (imports) ...
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se331.metricbackend.security.config.JwtService;
import se331.metricbackend.security.token.Token; // <-- ตรวจสอบว่านี่คือ Token Document
import se331.metricbackend.security.token.TokenRepository;
import se331.metricbackend.security.token.TokenType; // <-- Import TokenType
import se331.metricbackend.security.user.Role;
import se331.metricbackend.security.user.User; // <-- ตรวจสอบว่านี่คือ User Document
import se331.metricbackend.security.user.UserRepository;
import se331.metricbackend.util.LapMapper;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {


    String profileImage = null;
    if (request.getImage() != null && !request.getImage().isEmpty()) {
      profileImage = request.getImage().get(0);
    }

    User user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .username(request.getEmail())
            .enabled(true)
            .password(passwordEncoder.encode(request.getPassword()))
            .roles(List.of(Role.ROLE_READER))
            .profileImage(profileImage)
            .build();

    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);

    System.out.println("DEBUG images: " + savedUser.getProfileImage());


    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .user(LapMapper.INSTANCE.getUserReporterDto(savedUser))
            .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            )
    );
    User user = repository.findByUsername(request.getUsername())
            .orElseThrow();

    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    revokeAllUserTokens(user); // <-- 5. ตรวจสอบว่า user.getId() เป็น String
    saveUserToken(user, jwtToken);

    // 6. [ข้อควรระวัง] Mapper/DTO
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .user(LapMapper.INSTANCE.getUserReporterDto(user))
            .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    Token token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    // 7. [สำคัญมาก] เปลี่ยนการเรียก Repository
    // จาก findAllValidTokenByUser(user.getId())
    // เป็น Query Method ใหม่ที่เราสร้างขึ้น และส่ง String ID
    List<Token> validUserTokens = tokenRepository.findByUser_IdAndExpiredIsFalseAndRevokedIsFalse(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {

    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      User user = this.repository.findByUsername(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        String accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user); // <-- 8. ส่วนนี้จะทำงานถูกต้องแล้ว
        saveUserToken(user, accessToken);
        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}