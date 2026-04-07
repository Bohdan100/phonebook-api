package corp.phonebook.service

import org.springframework.data.domain.Pageable
import corp.phonebook.common.dto.PageResponse
import corp.phonebook.data.dto.ContactDTO
import corp.phonebook.data.dto.ContactStatisticsDTO
import corp.phonebook.data.entity.Contact

interface ContactService {
    fun getAllContactsOfUser(id: Long, pageable: Pageable): PageResponse<Contact>
    fun getById(id: Long): Contact
    fun getContactsByNumber(number: String, phonebookId: Long, pageable: Pageable): PageResponse<Contact>
    fun create(contactDTO: ContactDTO, phonebookId: Long): Contact
    fun update(id: Long, updateContact: ContactDTO): Contact
    fun delete(id: Long)
    fun getTotalContactsCount(userId: Long): Long
    fun getUniqueNumbersCount(userId: Long): Long
    fun getContactStatistics(userId: Long): ContactStatisticsDTO
}