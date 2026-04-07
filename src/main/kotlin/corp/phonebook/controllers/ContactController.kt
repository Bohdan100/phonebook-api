package corp.phonebook.controllers

import org.springframework.web.bind.annotation.*
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.http.ResponseEntity
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import jakarta.validation.Valid

import corp.phonebook.service.ContactService
import corp.phonebook.service.PhonebookService
import corp.phonebook.data.dto.ContactDTO
import corp.phonebook.data.entity.Contact
import corp.phonebook.data.entity.User
import corp.phonebook.data.entity.Role
import corp.phonebook.data.dto.ContactStatisticsDTO
import corp.phonebook.common.dto.PageResponse
import corp.phonebook.constants.Constants.VERSION
import corp.phonebook.exception.types.ResourceNotFoundException
import corp.phonebook.exception.types.ValidationException
import corp.phonebook.exception.types.AccessDeniedException
import corp.phonebook.exception.types.IllegalArgumentException

@RestController
@RequestMapping("$VERSION/phonebooks/contacts")
class ContactController(
    private val contactService: ContactService,
    private val phonebookService: PhonebookService,
) {
    // GET /api/v1/phonebooks/contacts/user/{id}?page=0&size=15&sort=name,asc
    @GetMapping("/user/{id}")
    fun getAllContactsOfUser(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: User?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "15") size: Int,
        @RequestParam(defaultValue = "name,asc") sort: String
    ): ResponseEntity<PageResponse<Contact>> {
        if (user?.id == null || id != user.id)
            throw AccessDeniedException("User ID does not match the authenticated user's ID.")

        val sortParams = sort.split(",")
        val sortDirection = if (sortParams.size > 1 && sortParams[1].lowercase() == "desc") {
            Sort.Direction.DESC
        } else {
            Sort.Direction.ASC
        }
        val sortField = sortParams[0]
        val pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField))
        val result = contactService.getAllContactsOfUser(id, pageable)

        return ResponseEntity.ok(result)
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

    // GET /api/v1/phonebooks/contacts/number/{number}?page=0&size=15&sort=name,asc
    @GetMapping("/number/{number}")
    fun getContactByNumber(
        @PathVariable number: String,
        @AuthenticationPrincipal user: User,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "15") size: Int,
        @RequestParam(defaultValue = "name,asc") sort: String
    ): ResponseEntity<PageResponse<Contact>> {
        val phonebookId = user.phoneBook?.id
            ?: throw ValidationException("User account is missing a phonebook profile.")

        val sortParams = sort.split(",")
        val sortDirection = if (sortParams.size > 1 && sortParams[1].lowercase() == "desc") {
            Sort.Direction.DESC
        } else {
            Sort.Direction.ASC
        }
        val sortField = sortParams[0]
        val pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField))
        val contacts = contactService.getContactsByNumber(number, phonebookId, pageable)

        return ResponseEntity.ok(contacts)
    }

    // GET /api/v1/phonebooks/contacts/statistics/total?userId=5
    @GetMapping("/statistics/total")
    fun getTotalContactsCount(
        @RequestParam userId: Long,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Map<String, Long>> {
        if (user.role != Role.ADMIN && userId != user.id) {
            throw AccessDeniedException("You can only view your own contact statistics")
        }

        val count = contactService.getTotalContactsCount(userId)
        return ResponseEntity.ok(mapOf("totalContacts" to count))
    }

    // GET /api/v1/phonebooks/contacts/statistics/unique?userId=5
    @GetMapping("/statistics/unique")
    fun getUniqueNumbersCount(
        @RequestParam userId: Long,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Map<String, Long>> {
        if (user.role != Role.ADMIN && userId != user.id) {
            throw AccessDeniedException("You can only view your own contact statistics")
        }

        val count = contactService.getUniqueNumbersCount(userId)
        return ResponseEntity.ok(mapOf("uniqueNumbers" to count))
    }

    // GET /api/v1/phonebooks/contacts/statistics/full?userId=5
    @GetMapping("/statistics/full")
    fun getContactStatistics(
        @RequestParam userId: Long,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<ContactStatisticsDTO> {
        if (user.role != Role.ADMIN && userId != user.id) {
            throw AccessDeniedException("You can only view your own contact statistics")
        }

        val statistics = contactService.getContactStatistics(userId)
        return ResponseEntity.ok(statistics)
    }

    @PostMapping
    fun create(
        @Valid @RequestBody contactDTO: ContactDTO?,
        @AuthenticationPrincipal user: User?
    ): ResponseEntity<Any> {
        if (contactDTO == null)
            throw IllegalArgumentException("Invalid input: Contact data is required.")

        val userId = user?.id ?: throw AccessDeniedException("User not authenticated.")

        val phonebookId = user.phoneBook?.id
            ?: throw ValidationException("User account is missing a phonebook profile.")

        if (!phonebookService.isPhonebookOwnedByUser(phonebookId, userId))
            throw AccessDeniedException("Access denied for the specified phonebook.")

        return ResponseEntity.status(201).body(contactService.create(contactDTO, phonebookId))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody contactDTO: ContactDTO?,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Any> {
        if (contactDTO == null)
            throw IllegalArgumentException("Invalid input: Contact data is required.")

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
            throw ResourceNotFoundException("Contact or User not found.")

        if (!phonebookService.isContactOwnedByUser(contact, userId))
            throw AccessDeniedException("Contact doesn't belong to the current user.")

        return null
    }
}