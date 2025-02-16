package corp.phonebook.service

import org.springframework.stereotype.Service
import corp.phonebook.data.repository.ContactRepository
import corp.phonebook.data.repository.PhoneBookRepository
import corp.phonebook.data.dto.ContactDTO
import corp.phonebook.data.entity.Contact
import corp.phonebook.errors.ResourceNotFoundException

@Service
class ContactService(
    private val contactRepository: ContactRepository,
    private val phoneBookRepository: PhoneBookRepository
) {
    fun getAllContactsOfUser(id: Long): List<Contact> {
        val phoneBook = phoneBookRepository.findPhoneBookByOwnerId(id)
        return phoneBook.contacts
    }

    fun getById(id: Long): Contact = contactRepository.findContactById(id)
        ?: throw ResourceNotFoundException("Contact not found.")


    fun getContactsByNumber(number: String, phonebookId: Long): List<Contact> =
        contactRepository.findContactsByNumberContainingAndPhoneBookId(number, phonebookId)

    fun create(contactDTO: ContactDTO): Contact {
        val phoneBook =
            phoneBookRepository.findPhoneBookById(contactDTO.phonebookId) ?: throw ResourceNotFoundException(
                "PhoneBook not found with id: ${contactDTO.phonebookId}"
            )

        val contact = Contact(
            name = contactDTO.name,
            number = contactDTO.number,
            phoneBook = phoneBook
        )
        return contactRepository.save(contact)
    }

    fun update(id: Long, updateContact: ContactDTO): Contact {
        if (updateContact.name.isEmpty() && updateContact.number.isEmpty())
            throw IllegalArgumentException("Invalid input: At least one field must be provided.")

        val contactFromDB =
            contactRepository.findContactById(id) ?: throw ResourceNotFoundException("Contact not found with id: $id")

        if (updateContact.name.isNotEmpty()) contactFromDB.name = updateContact.name
        if (updateContact.number.isNotEmpty()) contactFromDB.number = updateContact.number

        return contactRepository.save(contactFromDB)
    }

    fun delete(id: Long) {
        contactRepository.deleteById(id)
    }
}