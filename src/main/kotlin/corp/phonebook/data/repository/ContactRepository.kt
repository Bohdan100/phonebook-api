package corp.phonebook.data.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.JpaRepository
import corp.phonebook.data.entity.Contact

@Repository
interface ContactRepository : JpaRepository<Contact, Long> {
    @Query("SELECT c FROM Contact c WHERE c.id = :id")
    fun findContactById(@Param("id") id: Long): Contact?

    @Query("SELECT c FROM Contact c WHERE c.phoneBook.id = :phonebookId AND c.number LIKE %:number%")
    fun findContactsByNumberContainingAndPhoneBookId(
        @Param("number") number: String,
        @Param("phonebookId") phonebookId: Long
    ): List<Contact>
}