package corp.phonebook.service

import org.springframework.stereotype.Service
import corp.phonebook.data.repository.ContactRepository
import corp.phonebook.data.repository.PhoneBookRepository
import corp.phonebook.data.dto.ContactDTO
import corp.phonebook.data.entity.Contact

@Service
class ContactService(
    private val contactRepository: ContactRepository,
    private val phoneBookRepository: PhoneBookRepository
) {
    fun getById(id: Long): Contact? = contactRepository.findContactById(id)

    fun getContactsByNumber(number: String, phonebookId: Long): List<Contact> =
        contactRepository.findContactsByNumberContainingAndPhoneBookId(number, phonebookId)

    fun create(contactDTO: ContactDTO): Contact {
        val phoneBook = phoneBookRepository.findPhoneBookById(contactDTO.phonebookId) ?: throw IllegalArgumentException(
            "PhoneBook not found with id: ${contactDTO.phonebookId}"
        )

        val contact = Contact(
            name = contactDTO.name,
            number = contactDTO.number,
            phoneBook = phoneBook
        )
        return contactRepository.save(contact)
    }

    fun update(id: Long, newContact: ContactDTO): Contact {
        val contactFromDB =
            contactRepository.findContactById(id) ?: throw IllegalArgumentException("Contact not found with id: $id")

        if (newContact.name.isNotEmpty()) contactFromDB.name = newContact.name
        if (newContact.number.isNotEmpty()) contactFromDB.number = newContact.number

        return contactRepository.save(contactFromDB)
    }

    fun delete(id: Long) {
        contactRepository.deleteById(id)
    }
}