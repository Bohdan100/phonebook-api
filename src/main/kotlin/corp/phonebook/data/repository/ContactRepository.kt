package corp.phonebook.data.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import corp.phonebook.data.entity.Contact
import corp.phonebook.data.dto.ContactStatisticsDTO

@Repository
interface ContactRepository : JpaRepository<Contact, Long> {
    @Query("SELECT c FROM Contact c WHERE c.id = :id")
    fun findContactById(@Param("id") id: Long): Contact?

    @Query("SELECT c FROM Contact c WHERE c.phoneBook.id = :phonebookId")
    fun findByPhoneBookId(@Param("phonebookId") phonebookId: Long, pageable: Pageable): Page<Contact>

    @Query("SELECT c FROM Contact c WHERE c.phoneBook.id = :phonebookId AND c.number LIKE %:number%")
    fun findByNumberContainingAndPhoneBookId(
        @Param("number") number: String,
        @Param("phonebookId") phonebookId: Long,
        pageable: Pageable
    ): Page<Contact>

    @Query("SELECT COUNT(c) FROM Contact c WHERE c.phoneBook.owner.id = :userId")
    fun countContactsByUserId(@Param("userId") userId: Long): Long

    @Query("SELECT COUNT(DISTINCT c.number) FROM Contact c WHERE c.phoneBook.owner.id = :userId")
    fun countDistinctNumbersByUserId(@Param("userId") userId: Long): Long

    @Query("""
        SELECT new corp.phonebook.data.dto.ContactStatisticsDTO(
            COUNT(c),
            COUNT(DISTINCT c.number),
            MIN(LENGTH(c.name)),
            MAX(LENGTH(c.name)),
            AVG(LENGTH(c.name)),
            MIN(LENGTH(c.number)),
            MAX(LENGTH(c.number)),
            AVG(LENGTH(c.number))
        )
        FROM Contact c 
        WHERE c.phoneBook.owner.id = :userId
    """)
    fun getContactStatisticsByUserId(@Param("userId") userId: Long): ContactStatisticsDTO
}