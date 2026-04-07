package corp.phonebook.service.impl

import org.springframework.stereotype.Service
import corp.phonebook.service.PhonebookService
import corp.phonebook.data.repository.PhoneBookRepository
import corp.phonebook.data.entity.Contact

@Service
class PhonebookServiceImpl(
    private val phoneBookRepository: PhoneBookRepository
) : PhonebookService {

    override fun isPhonebookOwnedByUser(phonebookId: Long, userId: Long): Boolean {
        return phoneBookRepository.isPhonebookOwnedByUser(phonebookId, userId)
    }

    override fun isContactOwnedByUser(contact: Contact, userId: Long): Boolean {
        return contact.phoneBook.owner?.id == userId
    }
}