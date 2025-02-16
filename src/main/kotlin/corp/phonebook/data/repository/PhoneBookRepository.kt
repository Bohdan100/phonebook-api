package corp.phonebook.data.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.JpaRepository
import corp.phonebook.data.entity.PhoneBook
import corp.phonebook.data.entity.User

@Repository
interface PhoneBookRepository : JpaRepository<PhoneBook, Long> {
    @Query("SELECT pb FROM PhoneBook pb JOIN pb.owner o WHERE o.id = :ownerId")
    fun findPhoneBookByOwnerId(@Param("ownerId") ownerId: Long): PhoneBook

    @Query("SELECT pb FROM PhoneBook pb WHERE pb.id = :id")
    fun findPhoneBookById(@Param("id") id: Long): PhoneBook?

    @Query("SELECT DISTINCT pb.owner FROM PhoneBook pb")
    fun findAllOwners(): List<User>
}