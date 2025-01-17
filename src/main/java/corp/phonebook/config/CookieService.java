package corp.phonebook.config;

import org.springframework.stereotype.Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CookieService {

    public void addSessionCookie(HttpServletResponse response, String sessionId) {
        Cookie sessionCookie = new Cookie("SESSION_ID", sessionId);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(60 * 20);

        response.addCookie(sessionCookie);

        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(20);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedExpirationTime = expirationTime.format(formatter);
        formattedExpirationTime = URLEncoder.encode(formattedExpirationTime, StandardCharsets.UTF_8);

        Cookie expirationCookie = new Cookie("EXPIRED_COOKIE", formattedExpirationTime);
        expirationCookie.setHttpOnly(true);
        expirationCookie.setPath("/");
        expirationCookie.setMaxAge(60 * 20);

        response.addCookie(expirationCookie);
    }

    public void deleteSessionCookie(HttpServletResponse response) {
        Cookie logoutSessionCookie = new Cookie("SESSION_ID", null);
        logoutSessionCookie.setHttpOnly(true);
        logoutSessionCookie.setPath("/");
        logoutSessionCookie.setMaxAge(0);

        Cookie logoutTimeCookie = new Cookie("EXPIRED_COOKIE", null);
        logoutTimeCookie.setHttpOnly(true);
        logoutTimeCookie.setPath("/");
        logoutTimeCookie.setMaxAge(0);

        response.addCookie(logoutTimeCookie);
    }
}
