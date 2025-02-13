package corp.phonebook.service

import org.springframework.stereotype.Service
import corp.phonebook.data.repository.PhoneBookRepository
import corp.phonebook.data.entity.Contact

@Service
class PhonebookService(
    private val phoneBookRepository: PhoneBookRepository
) {
    fun isContactOwnedByUser(contact: Contact, userId: Long): Boolean {
        val userPhoneBook = phoneBookRepository.findPhoneBookById(userId)
        println("contact.phoneBook.id " + contact.phoneBook.id)
        if (userPhoneBook != null) {
            println("userPhoneBook.id " + userPhoneBook.id)
        }
        if (userPhoneBook != null) return contact.phoneBook.id == userPhoneBook.id

        return false
    }

    fun isPhonebookOwnedByUser(phonebookId: Long, userId: Long): Boolean {
        val phoneBook = phoneBookRepository.findPhoneBookById(phonebookId)
        val userPhoneBook = phoneBookRepository.findPhoneBookById(userId)

        return phoneBook != null && userPhoneBook != null && phoneBook.id == userPhoneBook.id
    }
}