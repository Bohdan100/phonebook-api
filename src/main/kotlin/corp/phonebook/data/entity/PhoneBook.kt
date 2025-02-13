package corp.phonebook.data.entity

import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable

@Entity
@Table(name = "phonebooks")
data class PhoneBook(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(orphanRemoval = true, mappedBy = "phoneBook")
    val owner: User? = null,

    @JsonIgnore
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "phoneBook")
    val contacts: List<Contact> = emptyList()
) : Serializable