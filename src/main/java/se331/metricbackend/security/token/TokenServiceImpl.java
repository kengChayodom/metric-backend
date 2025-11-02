package se331.metricbackend.security.token;

// 1. ลบ import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    final TokenDao tokenDao;

    // 2. ลบ @Transactional (JTA)
    @Override
    public void save(Token token) {
        tokenDao.save(token); // Atomic ต่อ Document
    }
}