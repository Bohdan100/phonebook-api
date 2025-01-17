package corp.phonebook.config;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import corp.phonebook.data.repository.AuthRepository;
import corp.phonebook.data.entity.User;

@Component
public class JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @Autowired
    AuthRepository authRepository;

    public String generateToken(String email) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 1000 * 60 * 60 * 2);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claimsResolvers.apply(claims);
    }

    public boolean isTokenValid(String token, User user) {
        String username = extractUserName(token);
        return username != null && username.equals(user.getEmail()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean isSessionExpired(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return true;
        }

        Optional<User> userOptional = authRepository.findBySessionId(sessionId);

        if (userOptional.isEmpty()) {
            return true;
        }

        User user = userOptional.get();
        LocalDateTime now = LocalDateTime.now();

        return user.getSessionExpiration() == null || user.getSessionExpiration().isBefore(now);
    }
}
