package corp.phonebook.service

import corp.phonebook.data.dto.LoginDTO
import corp.phonebook.data.dto.RegisterDTO
import corp.phonebook.data.entity.User

interface AuthService {
    fun register(request: RegisterDTO, sessionId: String): User
    fun login(request: LoginDTO, sessionId: String): User
    fun logout(sessionId: String)
}