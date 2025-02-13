package corp.phonebook.data.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import corp.phonebook.data.entity.User
import java.util.Optional

@Repository
interface AuthRepository : JpaRepository<User, Long> {
    fun findBySessionId(sessionId: String): Optional<User>
    fun findByEmail(email: String): Optional<User>
}