package corp.phonebook.controllers;

import corp.phonebook.data.dto.ContactDTO;
import corp.phonebook.data.entity.Contact;
import corp.phonebook.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import static corp.phonebook.constants.Constants.VERSION;
import static corp.phonebook.validation.Validation.requireNull;

@RestController
@RequestMapping(VERSION + "phonebooks/contacts")
public class ContactController {

    @Autowired
    ContactService contactService;

//  GET  http://localhost:8080/api/v1/phonebooks/contacts/4
    @GetMapping(value = "{id}")
    public ResponseEntity<Contact> getOne(@PathVariable("id") Long id) {
        Contact contact = contactService.get(id);
        if (requireNull(contact)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(contact, HttpStatus.OK);
        }
    }

//  GET  http://localhost:8080/api/v1/phonebooks/contacts/number/+81234567891
    @GetMapping(value = "/number/{number}")
    public ResponseEntity<Contact> getContactByNumber(@PathVariable("number") String number) {
        Contact contact = contactService.getContactByNumber(number);
        if (requireNull(contact)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(contact, HttpStatus.OK);
        }
    }

//  POST  http://localhost:8080/api/v1/phonebooks/contacts
//  { "name": "John Smith", "number": "+71234567890", "phonebookId": 1 }
    @PostMapping
    public ResponseEntity<Contact> create(@Valid @RequestBody ContactDTO contactDTO) {
        if (requireNull(contactDTO)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            Contact contact = contactService.create(contactDTO);
            return new ResponseEntity<>(contact, HttpStatus.CREATED);
        }
    }

//  UPDATE  http://localhost:8080/api/v1/phonebooks/contacts/1
//  { "name": "Mike Johnson Chicago", "number": "+7123456789" }
//  { "name": "Mike Johnson Chicago" } { "number": "+7123456789000" }
    @PutMapping(value = "{id}")
    public ResponseEntity<Contact> update(@PathVariable("id") Long id, @Valid @RequestBody Contact contact) {
        if (requireNull(contact)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            contactService.update(id, contact);
            return new ResponseEntity<>(contact, HttpStatus.OK);
        }
    }

//  DELETE  http://localhost:8080/api/v1/phonebooks/contacts/1
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Contact> delete(@PathVariable("id") Long id) {
        if(!contactService.isExist(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            contactService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
