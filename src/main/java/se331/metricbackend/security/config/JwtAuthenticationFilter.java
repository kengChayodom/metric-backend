package se331.metricbackend.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import se331.metricbackend.security.token.TokenRepository;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    // (ส่วน public endpoints... )
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      filterChain.doFilter(request, response);
      return;
    }
    final String uri = request.getRequestURI();
    if (uri.startsWith("/api/v1/auth")
            || "/uploadImage".equals(uri)
            || "/uploadFile".equals(uri)) {
      filterChain.doFilter(request, response);
      return;
    }

    // (ส่วนตรวจ header...)
    final String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    final String jwt = authHeader.substring(7);
    if (jwt.isBlank() || "null".equalsIgnoreCase(jwt) || "undefined".equalsIgnoreCase(jwt)) {
      filterChain.doFilter(request, response);
      return;
    }

    // VVVV นี่คือส่วนที่แก้ไขแล้ว VVVV
    try {
      final String userEmail = jwtService.extractUsername(jwt);

      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        boolean isTokenValid = tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

        if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities()
          );
          authToken.setDetails(
                  new WebAuthenticationDetailsSource().buildDetails(request)
          );
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }

      
      filterChain.doFilter(request, response);

    } catch (Exception ex) {
      // 2. ถ้า Token พัง/หมดอายุ (เกิด Exception) ให้ส่ง 401
      logger.warn("JWT validation failed: " + ex.getMessage());

      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType("application/json");
      response.getWriter().write("{\"error\": \"Authentication Failed\", \"message\": \"" + ex.getMessage() + "\"}");

      // 3. *ต้อง* return; เพื่อหยุดการทำงาน
      return;
    }
  }
}