package corp.phonebook.data.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import corp.phonebook.data.entity.User

@Repository
interface UserRepository : JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.id = :id")
    fun findUserById(@Param("id") id: Long): Optional<User>

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE CONCAT(LOWER(:name), '%')")
    fun findByNameStartingWithIgnoreCase(@Param("name") name: String): List<User>

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.id = :id")
    fun existsUserById(@Param("id") id: Long): Boolean

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.id != :id")
    fun existsUserByEmailAndNotId(@Param("email") email: String, @Param("id") id: Long): Boolean
}