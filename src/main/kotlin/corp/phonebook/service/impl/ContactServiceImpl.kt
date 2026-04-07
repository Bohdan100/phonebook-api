package corp.phonebook.service.impl

import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable
import corp.phonebook.service.ContactService
import corp.phonebook.data.dto.ContactDTO
import corp.phonebook.data.entity.Contact
import corp.phonebook.data.repository.ContactRepository
import corp.phonebook.data.repository.PhoneBookRepository
import corp.phonebook.common.dto.PageResponse
import corp.phonebook.data.dto.ContactStatisticsDTO
import corp.phonebook.exception.types.ResourceNotFoundException
import corp.phonebook.exception.types.ValidationException

@Service
class ContactServiceImpl(
    private val contactRepository: ContactRepository,
    private val phoneBookRepository: PhoneBookRepository
) : ContactService {
    override fun getAllContactsOfUser(id: Long, pageable: Pageable): PageResponse<Contact> {
        val phoneBook = phoneBookRepository.findPhoneBookByOwnerId(id)
            ?: throw ResourceNotFoundException("Phonebook for user $id not found.")

        val phoneBookId = phoneBook.id ?: throw ResourceNotFoundException("Phonebook ID is null for user $id")

        val page = contactRepository.findByPhoneBookId(phoneBookId, pageable)
        return PageResponse.from(page)
    }

    override fun getById(id: Long): Contact = contactRepository.findContactById(id)
        ?: throw ResourceNotFoundException("Contact with ID $id not found.")

    override fun getContactsByNumber(number: String, phonebookId: Long, pageable: Pageable): PageResponse<Contact> {
        val page = contactRepository.findByNumberContainingAndPhoneBookId(number, phonebookId, pageable)
        return PageResponse.from(page)
    }

    override fun create(contactDTO: ContactDTO, phonebookId: Long): Contact {
        val phoneBook = phoneBookRepository.findPhoneBookById(phonebookId)
            ?: throw ResourceNotFoundException("PhoneBook with ID $phonebookId not found.")

        return contactRepository.save(
            Contact(
                name = contactDTO.name,
                number = contactDTO.number,
                phoneBook = phoneBook
            )
        )
    }

    override fun update(id: Long, updateContact: ContactDTO): Contact {
        if (updateContact.name.isBlank() && updateContact.number.isBlank())
            throw ValidationException("Update failed: Name and number cannot both be empty.")

        val contactFromDB = getById(id)

        if (updateContact.name.isNotBlank()) contactFromDB.name = updateContact.name
        if (updateContact.number.isNotBlank()) contactFromDB.number = updateContact.number

        return contactRepository.save(contactFromDB)
    }

    override fun delete(id: Long) {
        if (!contactRepository.existsById(id))
            throw ResourceNotFoundException("Delete failed: Contact $id not found.")
        contactRepository.deleteById(id)
    }

    override fun getTotalContactsCount(userId: Long): Long {
        return contactRepository.countContactsByUserId(userId)
    }

    override fun getUniqueNumbersCount(userId: Long): Long {
        return contactRepository.countDistinctNumbersByUserId(userId)
    }

    override fun getContactStatistics(userId: Long): ContactStatisticsDTO {
        return contactRepository.getContactStatisticsByUserId(userId)
    }
}