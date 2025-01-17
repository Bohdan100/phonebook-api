package corp.phonebook.service;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import corp.phonebook.data.repository.ContactRepository;
import corp.phonebook.data.repository.PhoneBookRepository;

import corp.phonebook.data.dto.ContactDTO;
import corp.phonebook.data.entity.Contact;
import corp.phonebook.data.entity.PhoneBook;

@Service
@AllArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    private final PhoneBookRepository phoneBookRepository;

    public Contact get(Long id) {
            return contactRepository.findContactById(id);
    }

    public Contact getContactByNumber(String number) {
            return contactRepository.findContactByNumber(number);
    }

    public Contact create(ContactDTO contactDTO) {
        PhoneBook phoneBook = phoneBookRepository.findPhoneBookById(contactDTO.getPhonebookId());
        Contact contact = Contact.builder()
                .name(contactDTO.getName())
                .number(contactDTO.getNumber())
                .phoneBook(phoneBook)
                .build();

        return contactRepository.save(contact);
    }

    public Contact update(Long id, Contact newContact) {
        Contact contactFromDB = contactRepository.findContactById(id);
        if (newContact.getName() != null && !newContact.getName().isEmpty()) {
            contactFromDB.setName(newContact.getName());
        }
        if (newContact.getNumber() != null && !newContact.getNumber().isEmpty()) {
            contactFromDB.setNumber(newContact.getNumber());
        }

        return contactRepository.save(contactFromDB);
    }
    
    public void delete(Long id) {
        contactRepository.deleteById(id);
    }

    public boolean isExist(Long id) {
        return contactRepository.existsContactById(id);
    }
}
