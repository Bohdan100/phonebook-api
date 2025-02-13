package corp.phonebook.config

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import corp.phonebook.data.repository.AuthRepository

@Component
class SessionService(
    private val authRepository: AuthRepository
) {

    fun isSessionExpired(sessionId: String): Boolean {
        if (sessionId.isBlank()) return true

        val userOptional = authRepository.findBySessionId(sessionId)
        if (userOptional.isEmpty) return true

        val user = userOptional.get()
        val now = LocalDateTime.now()

        val sessionExpiration = user.sessionExpiration
        return sessionExpiration == null || sessionExpiration.isBefore(now)
    }
}