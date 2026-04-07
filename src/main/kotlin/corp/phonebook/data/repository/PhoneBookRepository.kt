package corp.phonebook.data.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import corp.phonebook.data.entity.PhoneBook
import corp.phonebook.data.entity.User

@Repository
interface PhoneBookRepository : JpaRepository<PhoneBook, Long> {
    @Query("SELECT pb FROM PhoneBook pb JOIN pb.owner o WHERE o.id = :ownerId")
    fun findPhoneBookByOwnerId(@Param("ownerId") ownerId: Long): PhoneBook?

    @Query("SELECT pb FROM PhoneBook pb WHERE pb.id = :id")
    fun findPhoneBookById(@Param("id") id: Long): PhoneBook?

    @Query("SELECT COUNT(pb) > 0 FROM PhoneBook pb WHERE pb.id = :phonebookId AND pb.owner.id = :userId")
    fun isPhonebookOwnedByUser(
        @Param("phonebookId") phonebookId: Long,
        @Param("userId") userId: Long
    ): Boolean

    @Query(
        value = "SELECT DISTINCT u FROM User u WHERE u.phoneBook IS NOT NULL",
        countQuery = "SELECT COUNT(DISTINCT u) FROM User u WHERE u.phoneBook IS NOT NULL"
    )
    fun findAllOwners(pageable: Pageable): Page<User>
}