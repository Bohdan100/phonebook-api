package corp.phonebook.data.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import corp.phonebook.data.entity.User
import corp.phonebook.data.dto.UserStatisticsDTO

@Repository
interface UserRepository : JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.id = :id")
    fun findUserById(@Param("id") id: Long): Optional<User>

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE CONCAT(LOWER(:name), '%')")
    fun findByNameStartingWithIgnoreCase(
        @Param("name") name: String,
        pageable: Pageable
    ): Page<User>

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.id = :id")
    fun existsUserById(@Param("id") id: Long): Boolean

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.id != :id")
    fun existsUserByEmailAndNotId(
        @Param("email") email: String,
        @Param("id") id: Long
    ): Boolean

    @Query("SELECT COUNT(u) FROM User u")
    fun countTotalUsers(): Long

    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    fun countUsersByRole(): List<Array<Any>>

    @Query("SELECT COUNT(u) FROM User u WHERE u.sessionId IS NOT NULL AND u.sessionExpiration > CURRENT_TIMESTAMP")
    fun countActiveSessions(): Long

    @Query("""
        SELECT new corp.phonebook.data.dto.UserStatisticsDTO(
            COUNT(u),
            COUNT(DISTINCT u.email),
            MIN(LENGTH(u.name)),
            MAX(LENGTH(u.name)),
            AVG(LENGTH(u.name)),
            COUNT(CASE WHEN u.role = corp.phonebook.data.entity.Role.ADMIN THEN 1 END),
            COUNT(CASE WHEN u.role = corp.phonebook.data.entity.Role.USER THEN 1 END),
            COUNT(CASE WHEN u.sessionId IS NOT NULL AND u.sessionExpiration > CURRENT_TIMESTAMP THEN 1 END)
        )
        FROM User u
    """)
    fun getUserStatistics(): UserStatisticsDTO
}