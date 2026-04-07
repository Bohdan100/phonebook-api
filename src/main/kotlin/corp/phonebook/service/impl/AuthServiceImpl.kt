package corp.phonebook.service.impl

import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import corp.phonebook.service.AuthService
import corp.phonebook.service.UserAuditService
import corp.phonebook.data.repository.AuthRepository
import corp.phonebook.data.dto.RegisterDTO
import corp.phonebook.data.dto.LoginDTO
import corp.phonebook.data.entity.Role
import corp.phonebook.data.entity.User
import corp.phonebook.exception.types.EmailAlreadyExistsException
import corp.phonebook.exception.types.UnauthorizedException

@Service
class AuthServiceImpl(
    private val authRepository: AuthRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userAuditService: UserAuditService
) : AuthService {

    override fun register(request: RegisterDTO, sessionId: String): User {
        if (authRepository.findByEmail(request.email).isPresent)
            throw EmailAlreadyExistsException("Email ${request.email} is already registered.")

        val role = try {
            if (request.role != null) Role.valueOf(request.role.uppercase()) else Role.USER
        } catch (e: Exception) {
            Role.USER
        }

        val encodedPassword = passwordEncoder.encode(request.password)
            ?: throw RuntimeException("Failed to process security credentials.")

        val user = User(
            name = request.name,
            email = request.email,
            password = encodedPassword,
            sessionId = sessionId,
            role = role
        )
        user.sessionExpiration = LocalDateTime.now().plusMinutes(30)

        val savedUser = authRepository.save(user)
        userAuditService.saveRegister(savedUser.email)

        return savedUser
    }

    override fun login(request: LoginDTO, sessionId: String): User {
        val user = authRepository.findByEmail(request.email)
            .orElseThrow { UnauthorizedException("Invalid email or password.") }

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            throw UnauthorizedException("Invalid email or password.")
        }

        user.sessionId = sessionId
        user.sessionExpiration = LocalDateTime.now().plusMinutes(20)

        return authRepository.save(user)
    }

    override fun logout(sessionId: String) {
        val user = authRepository.findBySessionId(sessionId)
            .orElseThrow { UnauthorizedException("Active session not found.") }

        user.sessionId = null
        user.sessionExpiration = null
        authRepository.save(user)
    }
}