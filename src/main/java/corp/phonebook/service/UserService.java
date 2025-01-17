package corp.phonebook.service;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import corp.phonebook.data.repository.UserRepository;
import corp.phonebook.data.repository.PhoneBookRepository;
import corp.phonebook.data.entity.User;
import corp.phonebook.data.dto.UserDTO;
import corp.phonebook.data.entity.Contact;
import corp.phonebook.data.entity.PhoneBook;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PhoneBookRepository phoneBookRepository;

    public List<User> getAllOwners() {
        List<PhoneBook> phoneBooks = phoneBookRepository.findAll();
        List<User> owners = new ArrayList<>();

        for (PhoneBook phoneBook : phoneBooks) {
            User user = phoneBook.getOwner();
            owners.add(user);
        }
        return owners;
    }

    public User getById(long id) {
        return userRepository.findUserById(id);
    }

    public List<User> getByName(String name) {
        return userRepository.findByNameStartingWithIgnoreCase(name);
    }

    public User update(Long id, UserDTO userDTO) {
        User userFromDB = userRepository.findUserById(id);
        if (userFromDB == null) {
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(userFromDB.getEmail())) {
            boolean emailExists = userRepository.existsUserByEmailAndNotId(userDTO.getEmail(), id);
            if (emailExists) {
                throw new IllegalArgumentException("Email already in use by another user.");
            }
            userFromDB.setEmail(userDTO.getEmail());
        }

        if (userDTO.getName() != null) {
            userFromDB.setName(userDTO.getName());
        }

        return userRepository.save(userFromDB);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean isExist(Long id) {
        return userRepository.existsUserById(id);
    }

    public List<Contact> getAllUserContacts(Long id) {
        PhoneBook phoneBook = phoneBookRepository.findPhoneBookByOwner_Id(id);
        return phoneBook.getContacts();
    }
}
