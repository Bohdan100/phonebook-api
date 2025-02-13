package corp.phonebook.config

import org.springframework.stereotype.Service
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class CookieService {
    fun addSessionCookie(response: HttpServletResponse, sessionId: String) {
        val sessionCookie = Cookie("SESSION_ID", sessionId).apply {
            isHttpOnly = true
            path = "/"
            maxAge = 60 * 20
        }
        response.addCookie(sessionCookie)

        val expirationTime = LocalDateTime.now().plusMinutes(20)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedExpirationTime = URLEncoder.encode(expirationTime.format(formatter), StandardCharsets.UTF_8)

        val expirationCookie = Cookie("EXPIRED_COOKIE", formattedExpirationTime).apply {
            isHttpOnly = true
            path = "/"
            maxAge = 60 * 20
        }
        response.addCookie(expirationCookie)
    }

    fun deleteSessionCookie(response: HttpServletResponse) {
        val logoutSessionCookie = Cookie("SESSION_ID", null).apply {
            isHttpOnly = true
            path = "/"
            maxAge = 0
        }
        val logoutTimeCookie = Cookie("EXPIRED_COOKIE", null).apply {
            isHttpOnly = true
            path = "/"
            maxAge = 0
        }

        response.addCookie(logoutSessionCookie)
        response.addCookie(logoutTimeCookie)
    }
}