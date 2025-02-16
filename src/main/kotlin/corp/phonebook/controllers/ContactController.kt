package corp.phonebook.controllers

import org.springframework.web.bind.annotation.*
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.access.AccessDeniedException
import org.springframework.http.ResponseEntity
import jakarta.validation.Valid

import corp.phonebook.service.ContactService
import corp.phonebook.service.PhonebookService
import corp.phonebook.data.dto.ContactDTO
import corp.phonebook.data.entity.Contact
import corp.phonebook.data.entity.User
import corp.phonebook.constants.Constants.VERSION
import corp.phonebook.errors.ResourceNotFoundException

@RestController
@RequestMapping("$VERSION/phonebooks/contacts")
class ContactController(
    private val contactService: ContactService,
    private val phonebookService: PhonebookService,
) {
    @GetMapping("/user/{id}")
    fun getAllContactsOfUser(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: User?
    ): ResponseEntity<Any> {
        if (user?.id == null || id != user.id)
            throw AccessDeniedException("User ID does not match the authenticated user's ID.")

        return ResponseEntity.ok(contactService.getAllContactsOfUser(id))
    }

    @GetMapping("/{id}")
    fun getContactById(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Any> {
        val contact = contactService.getById(id)
        val validationResult = validateContactAndUser(contact, user.id)

        return validationResult ?: ResponseEntity.ok(contact)
    }

    @GetMapping("/number/{number}")
    fun getContactByNumber(
        @PathVariable number: String,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Any> {
        val phonebookId = user.phoneBook?.id ?: throw IllegalArgumentException("User does not have a phonebook.")
        val contacts = contactService.getContactsByNumber(number, phonebookId)

        return ResponseEntity.ok(contacts)
    }

    @PostMapping
    fun create(
        @Valid @RequestBody contactDTO: ContactDTO?,
        @AuthenticationPrincipal user: User?
    ): ResponseEntity<Any> {
        if (contactDTO == null)
            throw IllegalArgumentException("Invalid input: Contact data is required.")

        val userId = user?.id
        if (userId == null || !phonebookService.isPhonebookOwnedByUser(contactDTO.phonebookId, userId))
            throw AccessDeniedException("Access denied for the specified phonebook.")

        return ResponseEntity.status(201).body(contactService.create(contactDTO))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody contactDTO: ContactDTO?,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Any> {
        if (contactDTO == null) throw IllegalArgumentException("Invalid input: Contact data is required.")

        val contact = contactService.getById(id)
        val validationResult = validateContactAndUser(contact, user.id)

        return validationResult ?: ResponseEntity.ok(contactService.update(id, contactDTO))
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: User?
    ): ResponseEntity<Any> {
        val contact = contactService.getById(id)
        val validationResult = validateContactAndUser(contact, user?.id)
        if (validationResult != null) return validationResult

        contactService.delete(id)
        return ResponseEntity.noContent().build()
    }

    private fun validateContactAndUser(contact: Contact?, userId: Long?): ResponseEntity<Any>? {
        if (contact == null || userId == null)
            throw ResourceNotFoundException("Contact not found by your request.")

        if (!phonebookService.isContactOwnedByUser(contact, userId))
            throw AccessDeniedException("Contact couldn't be found for the current user.")

        return null
    }
}