package corp.phonebook.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.AllArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import corp.phonebook.service.PhonebookService;
import corp.phonebook.service.ContactService;
import corp.phonebook.service.UserService;
import corp.phonebook.data.entity.User;
import corp.phonebook.data.entity.Contact;
import corp.phonebook.data.dto.ContactDTO;
import java.util.List;

import static corp.phonebook.constants.Constants.VERSION;
import static corp.phonebook.validation.Validation.requireNull;

@RestController
@AllArgsConstructor
@RequestMapping(VERSION + "phonebooks/contacts")
public class ContactController {

    private final ContactService contactService;

    private final PhonebookService phonebookService;

    private final UserService userService;

    // GET all contacts by userId  http://localhost:8080/api/v1/phonebooks/contacts/user/2
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<?> getAllContactsOfUser(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal User user) {
        if (!id.equals(user.getId())) {
            return ResponseEntity.status(403).body("Access denied: User ID does not match the authenticated user's ID.");
        }

        List<Contact> allUserContacts = userService.getAllUserContacts(id);
        if (allUserContacts.isEmpty()) {
            return ResponseEntity.status(404).body("No contacts found for user with id: " + id);
        }
        return ResponseEntity.ok(allUserContacts);
    }

    // GET by id http://localhost:8080/api/v1/phonebooks/contacts/4
    @GetMapping(value = "{id}")
    public ResponseEntity<?> getContactById(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal User user) {
        Contact contact = contactService.get(id);
        if (requireNull(contact) || phonebookService.isContactOwnedByUser(contact, user.getId())) {
            return ResponseEntity.status(404).body("For current user contact not found with id: " + id);
        } else {
            return ResponseEntity.ok(contact);
        }
    }

    //  GET by number http://localhost:8080/api/v1/phonebooks/contacts/number/+81234567891
    @GetMapping(value = "/number/{number}")
    public ResponseEntity<?> getContactByNumber(
            @PathVariable("number") String number,
            @AuthenticationPrincipal User user) {
        Contact contact = contactService.getContactByNumber(number);
        if (requireNull(contact) || phonebookService.isContactOwnedByUser(contact, user.getId())) {
            return ResponseEntity.status(404).body("Contact not found with number: " + number);
        } else {
            return ResponseEntity.ok(contact);
        }
    }

    //  POST  http://localhost:8080/api/v1/phonebooks/contacts
//  { "name": "John Smith", "number": "+71234567890", "phonebookId": 1 }
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody ContactDTO contactDTO,
            @AuthenticationPrincipal User user) {
        if (requireNull(contactDTO)) {
            return ResponseEntity.badRequest().body("Invalid input: Contact data is required.");
        }

        if (!phonebookService.isPhonebookOwnedByUser(contactDTO.getPhonebookId(), user.getId())) {
            return ResponseEntity.status(403).body("Access denied for phonebook with id: " + contactDTO.getPhonebookId());
        }

            Contact contact = contactService.create(contactDTO);
            return ResponseEntity.status(201).body(contact);
    }

    //  UPDATE  http://localhost:8080/api/v1/phonebooks/contacts/1
//  { "name": "Mike Johnson Chicago", "number": "+7123456789" }
//  { "name": "Mike Johnson Chicago" } { "number": "+7123456789000" }
    @PutMapping(value = "{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody Contact contact,
            @AuthenticationPrincipal User user) {
        if (requireNull(contact)) {
            return ResponseEntity.badRequest().body("Invalid input: Contact data is required.");
        }

        if (phonebookService.isContactIdOwnedByUser(id, user.getId())) {
            return ResponseEntity.status(403).body("Access denied for contact with id: " + id);
        }

        try {
            Contact updatedContact = contactService.update(id, contact);
            return ResponseEntity.ok(updatedContact);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    //  DELETE  http://localhost:8080/api/v1/phonebooks/contacts/1
    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal User user) {
        if (!contactService.isExist(id)) {
            return ResponseEntity.status(404).body("Contact not found with id: " + id);
        }

        if (phonebookService.isContactIdOwnedByUser(id, user.getId())) {
            return ResponseEntity.status(403).body("Access denied for contact with id: " + id);
        }

        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
