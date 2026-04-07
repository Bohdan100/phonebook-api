package corp.phonebook.data.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_audit")
data class UserAudit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 20)
    val operation: String,

    @Column(name = "user_id")
    val userId: Long? = null,

    @Column(name = "user_email", nullable = false)
    val userEmail: String,

    @Column(name = "operation_time", nullable = false)
    val operationTime: LocalDateTime = LocalDateTime.now()
)