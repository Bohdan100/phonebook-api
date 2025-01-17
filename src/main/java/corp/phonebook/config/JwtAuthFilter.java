package corp.phonebook.config;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;

import corp.phonebook.data.repository.AuthRepository;
import corp.phonebook.data.entity.User;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final AuthRepository authRepository;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        Cookie sessionCookie = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(cookie -> "SESSION_ID".equals(cookie.getName()))
                .findFirst().orElse(null);

        if (authHeader == null || !authHeader.startsWith("Bearer ") || sessionCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String sessionId = sessionCookie.getValue();
        if (jwtService.isSessionExpired(sessionId)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        String email = jwtService.extractUserName(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = authRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (jwtService.isTokenValid(token, user)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
