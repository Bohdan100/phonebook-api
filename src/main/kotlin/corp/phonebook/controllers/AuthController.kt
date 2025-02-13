package corp.phonebook.controllers

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import java.util.UUID

import corp.phonebook.config.CookieService
import corp.phonebook.service.AuthService
import corp.phonebook.data.dto.LoginDTO
import corp.phonebook.data.dto.RegisterDTO
import corp.phonebook.constants.Constants.VERSION

@RestController
@RequestMapping("$VERSION/auth")
class AuthController(
    private val authService: AuthService,
    private val cookieService: CookieService
) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterDTO,
        response: HttpServletResponse
    ): ResponseEntity<Map<String, String>> {
        val sessionId = UUID.randomUUID().toString()
        authService.register(request, sessionId)
        cookieService.addSessionCookie(response, sessionId)

        return ResponseEntity.status(HttpStatus.CREATED).body(mapOf("sessionId" to sessionId))
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginDTO,
        response: HttpServletResponse
    ): ResponseEntity<Map<String, String>> {
        val sessionId = UUID.randomUUID().toString()
        authService.login(request, sessionId)
        cookieService.addSessionCookie(response, sessionId)

        return ResponseEntity.ok(mapOf("sessionId" to sessionId))
    }

    @PostMapping("/logout")
    fun logout(
        @CookieValue(name = "SESSION_ID", required = false) sessionId: String?,
        response: HttpServletResponse
    ): ResponseEntity<String> {
        if (sessionId.isNullOrBlank()) return ResponseEntity.badRequest().body("Session ID is missing")

        authService.logout(sessionId)
        cookieService.deleteSessionCookie(response)

        return ResponseEntity.ok("Logged out successfully")
    }
}