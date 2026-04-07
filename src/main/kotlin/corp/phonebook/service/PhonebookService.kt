package corp.phonebook.service

import corp.phonebook.data.entity.Contact

interface PhonebookService {
    fun isPhonebookOwnedByUser(phonebookId: Long, userId: Long): Boolean
    fun isContactOwnedByUser(contact: Contact, userId: Long): Boolean
}