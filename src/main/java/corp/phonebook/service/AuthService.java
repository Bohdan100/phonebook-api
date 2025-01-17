package corp.phonebook.service;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import corp.phonebook.config.JwtService;

import corp.phonebook.data.repository.AuthRepository;
import corp.phonebook.data.entity.User;
import corp.phonebook.data.entity.Role;
import corp.phonebook.data.dto.RegisterRequestDTO;
import corp.phonebook.data.dto.LoginRequestDTO;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(RegisterRequestDTO request, String sessionId, Role role) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .sessionId(sessionId)
                .role(role)
                .build();

        user.setSessionExpiration(LocalDateTime.now().plusMinutes(20));

        String token = jwtService.generateToken(user.getEmail());
        user.setToken(token);

        authRepository.save(user);

        return token;
    }

    public String login(LoginRequestDTO request, String sessionId) {
        User user = authRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        user.setSessionId(sessionId);
        // к текущему времени добавляем durationMinutes - 20 минут и записываем в БД время на 20 минут больше
        user.setSessionExpiration(LocalDateTime.now().plusMinutes(20));

        String token = jwtService.generateToken(user.getEmail());
        user.setToken(token);
        authRepository.save(user);

        return token;
    }

    public void logout(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token is required");
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            throw new IllegalArgumentException("Invalid token format");
        }

        User user = authRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        user.setToken(null);
        user.setSessionId(null);

        authRepository.save(user);
    }
}
