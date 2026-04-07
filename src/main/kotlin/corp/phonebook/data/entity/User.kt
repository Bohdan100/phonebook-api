package corp.phonebook.data.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime

@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_users_session_id", columnList = "session_id"),
        Index(name = "idx_users_name", columnList = "name")]
)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @get:JvmName("user_password")
    @Column(nullable = false)
    private val password: String,

    @Column(name = "session_id", nullable = true)
    var sessionId: String? = null,

    @Column(name = "session_expiration", nullable = true)
    var sessionExpiration: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role,

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "phonebook_id", nullable = false)
    val phoneBook: PhoneBook? = null
) : UserDetails {

    @JsonIgnore
    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(role.authority))

    @JsonIgnore
    override fun getUsername(): String = email

    @JsonIgnore
    override fun getPassword(): String = password

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean = true

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean = true

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean = true

    @JsonIgnore
    override fun isEnabled(): Boolean = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}