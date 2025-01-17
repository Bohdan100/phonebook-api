package corp.phonebook.service;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import corp.phonebook.data.repository.PhoneBookRepository;
import corp.phonebook.data.repository.ContactRepository;
import corp.phonebook.data.entity.PhoneBook;
import corp.phonebook.data.entity.Contact;

@Service
@AllArgsConstructor
public class PhonebookService {
    private final ContactRepository contactRepository;

    private final PhoneBookRepository phoneBookRepository;

    public boolean isContactIdOwnedByUser(Long contactId, Long userId) {
        Contact contact = contactRepository.findContactById(contactId);
        return contact != null && isContactOwnedByUser(contact, userId);
    }

    public boolean isContactOwnedByUser(Contact contact, Long userId) {
        if (contact != null && contact.getPhoneBook() != null) {
            PhoneBook userPhoneBook = phoneBookRepository.findPhoneBookById(userId);
            if (userPhoneBook != null) {
                return !contact.getPhoneBook().getId().equals(userPhoneBook.getId());
            }
        }
        return false;
    }

    public boolean isPhonebookOwnedByUser(Long phonebookId, Long userId) {
        PhoneBook phoneBook = phoneBookRepository.findPhoneBookById(phonebookId);
        PhoneBook userPhoneBook = phoneBookRepository.findPhoneBookById(userId);

        if (phoneBook != null && userPhoneBook != null) {
            return phoneBook.getId().equals(userPhoneBook.getId());
        }
        return false;
    }
}
