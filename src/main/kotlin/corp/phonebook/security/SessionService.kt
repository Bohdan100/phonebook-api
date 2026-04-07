package corp.phonebook.security

import org.springframework.stereotype.Service
import java.time.LocalDateTime
import corp.phonebook.data.repository.AuthRepository

@Service
class SessionService(
    private val authRepository: AuthRepository
) {
    fun isSessionExpired(sessionId: String): Boolean {
        if (sessionId.isBlank()) {
            return true
        }

        val userOptional = authRepository.findBySessionId(sessionId)
        if (userOptional.isEmpty) {
            return true
        }

        val user = userOptional.get()
        val now = LocalDateTime.now()
        val sessionExpiration = user.sessionExpiration

        val isExpired = sessionExpiration == null || sessionExpiration.isBefore(now)

        return isExpired
    }
}