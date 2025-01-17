package corp.phonebook.controllers;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import corp.phonebook.service.AuthService;
import corp.phonebook.config.CookieService;
import corp.phonebook.data.dto.LoginRequestDTO;
import corp.phonebook.data.dto.RegisterRequestDTO;
import corp.phonebook.data.entity.Role;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieService cookieService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequestDTO request,
            HttpServletResponse response) {
        Role role = (request.getRole() != null && (request.getRole().equals("ADMIN") || request.getRole().equals("USER"))) ?
                Role.valueOf(request.getRole()) : Role.USER;

        String sessionId = UUID.randomUUID().toString();

        String token = authService.register(request, sessionId, role);
        cookieService.addSessionCookie(response, sessionId);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequestDTO request,
            HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();

        String token = authService.login(request, sessionId);
        cookieService.addSessionCookie(response, sessionId);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletResponse response) {
        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity.badRequest().body("Authorization header is missing");
        }

        authService.logout(authHeader);
        cookieService.deleteSessionCookie(response);

        return ResponseEntity.ok("Logged out successfully");
    }
}
