package corp.phonebook.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import corp.phonebook.data.repository.UserRepository;
import corp.phonebook.data.repository.PhoneBookRepository;

import corp.phonebook.data.entity.Contact;
import corp.phonebook.data.entity.PhoneBook;
import corp.phonebook.data.entity.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PhoneBookRepository phoneBookRepository;

    public User get(long id) {
            return userRepository.findUserById(id);
    }

    public List<User> getAllOwners() {
            List<PhoneBook> phoneBooks = phoneBookRepository.findAll();
            List<User> owners = new ArrayList<>();
            for (PhoneBook phoneBook : phoneBooks) {
                User user = phoneBook.getOwner();
                owners.add(user);
            }
            return owners;
    }

    public List<Contact> getAllUserContacts(Long id) {
            PhoneBook phoneBook = phoneBookRepository.findPhoneBookByOwner_Id(id);
            return phoneBook.getContacts();
    }

    public List<User> getByName(String name) {
            return userRepository.findByNameStartingWithIgnoreCase(name);
    }

    public void create(User user) {
        userRepository.save(user);
    }

    public void update(Long id, User newUser) {
        User userFromDB = userRepository.findUserById(id);
        String name = newUser.getName();
        userFromDB.setName(name);
        userRepository.save(userFromDB);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean isExist(Long id) {
        return userRepository.existsUserById(id);
    }

}
