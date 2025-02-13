package corp.phonebook.data.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable

@Entity
@Table(name = "contacts")
data class Contact(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    var name: String,

    @Column(nullable = false)
    @Pattern(regexp = "^\\s*\\+?\\s*[\\d\\s]{3,15}\\s*$")
    var number: String,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phonebook_id", nullable = false)
    var phoneBook: PhoneBook
) : Serializable