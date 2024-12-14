package corp.phonebook.controllers;

import corp.phonebook.data.entity.Contact;
import corp.phonebook.data.entity.User;
import corp.phonebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static corp.phonebook.constants.Constants.VERSION;
import static corp.phonebook.validation.Validation.requireNull;

// json objects
@RestController
@RequestMapping(VERSION + "phonebooks/users")
public class UserController {

    @Autowired
    UserService userService;

    // GET all users  http://localhost:8080/api/v1/phonebooks/users/owners
    @GetMapping(value = "/owners")
    public ResponseEntity<List<User>> list() {
        List<User> users = userService.getAllOwners();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    // GET user contacts by id  http://localhost:8080/api/v1/phonebooks/users/2/contacts
    @GetMapping(value = "{id}/contacts")
    public ResponseEntity<List<Contact>> getAllUserContacts(@PathVariable("id") Long id) {
        List<Contact> allUserContacts = userService.getAllUserContacts(id);
        if (allUserContacts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(allUserContacts, HttpStatus.OK);
        }
    }

    // GET user by name  http://localhost:8080/api/v1/phonebooks/users/names/Bob
    @GetMapping(value = {"names/{name}"})
    public ResponseEntity<List<User>> getUserByName(@PathVariable("name") String name) {
        List<User> user = userService.getByName(name);
        if (requireNull(user)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    // GET user by id  http://localhost:8080/api/v1/phonebooks/users/2
    @GetMapping(value = "{id}")
    public ResponseEntity<User> getOne(@PathVariable("id") Long id) {
        User user = userService.get(id);
        if (requireNull(user)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    // DELETE user by id  http://localhost:8080/api/v1/phonebooks/users/2
    @DeleteMapping(value = "{id}")
    public ResponseEntity<User> delete(@PathVariable("id") Long id) {
        if (!userService.isExist(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            userService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    // CREATE user  http://localhost:8080/api/v1/phonebooks/users
    // { "name": "Goodwin" }
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        if (requireNull(user)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            userService.create(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
    }

    // UPDATE user by id  http://localhost:8080/api/v1/phonebooks/users/1
    // { "id": 1, "name": "Alice + Bob" }
    @PutMapping(value = "{id}")
    public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody User user) {
        if (requireNull(user)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            userService.update(id, user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

}

