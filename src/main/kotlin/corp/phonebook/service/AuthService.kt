package corp.phonebook.service

import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.time.LocalDateTime

import corp.phonebook.data.repository.AuthRepository
import corp.phonebook.data.dto.RegisterDTO
import corp.phonebook.data.dto.LoginDTO
import corp.phonebook.data.entity.User
import corp.phonebook.data.entity.Role
import corp.phonebook.errors.EmailAlreadyExistsException

@Service
class AuthService(
    private val authRepository: AuthRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun register(request: RegisterDTO, sessionId: String): User {
        if (authRepository.findByEmail(request.email).isPresent) {
            throw EmailAlreadyExistsException("Email already exists")
        }

        val role = if (request.role != null && (request.role == "ADMIN" || request.role == "USER")) {
            Role.valueOf(request.role)
        } else {
            Role.USER
        }

        val user = User(
            name = request.name,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            sessionId = sessionId,
            role = role
        )
        user.sessionExpiration = LocalDateTime.now().plusMinutes(20)

        return authRepository.save(user)
    }

    fun login(request: LoginDTO, sessionId: String): User {
        val user = authRepository.findByEmail(request.email)
            .orElseThrow { UsernameNotFoundException("Invalid email or password") }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BadCredentialsException("Invalid email or password")
        }

        user.sessionId = sessionId
        user.sessionExpiration = LocalDateTime.now().plusMinutes(20)

        return authRepository.save(user)
    }

    fun logout(sessionId: String) {
        if (sessionId.isBlank()) {
            throw IllegalArgumentException("Session ID is required")
        }

        val user = authRepository.findBySessionId(sessionId)
            .orElseThrow { IllegalArgumentException("Invalid session ID") }

        user.sessionId = null
        user.sessionExpiration = null
        authRepository.save(user)
    }
}