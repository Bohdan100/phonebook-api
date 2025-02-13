package corp.phonebook.controllers

import org.springframework.web.bind.annotation.*
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.access.annotation.Secured
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException

import corp.phonebook.service.UserService
import corp.phonebook.data.dto.UserDTO
import corp.phonebook.data.entity.User
import corp.phonebook.constants.Constants.VERSION
import corp.phonebook.data.entity.Role

@RestController
@RequestMapping("$VERSION/phonebooks/users")
class UserController(
    private val userService: UserService
) {
    @Secured("ROLE_ADMIN")
    @GetMapping("/owners")
    fun getAllOwnersOfPhonebooks(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Any> {
        val ownersOfPhonebooks = userService.getAllOwners()
        if (ownersOfPhonebooks.isEmpty()) throw IllegalArgumentException("No owners found.")

        return ResponseEntity.ok(ownersOfPhonebooks)
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/names/{name}")
    fun getUserByName(
        @PathVariable name: String,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Any> {
        val users = userService.getByName(name)
        if (users.isEmpty()) throw IllegalArgumentException("No users found with name: $name")

        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Any> {
        if (user.role != Role.ADMIN && id != user.id) throw AccessDeniedException("Access denied: User ID does not match the authenticated user's ID.")

        val foundUser = userService.getById(id)
        if (foundUser == null) throw IllegalArgumentException("User not found.")

        return ResponseEntity.ok(foundUser)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody userDTO: UserDTO?,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Any> {
        if (id != user.id) throw AccessDeniedException("Access denied: User ID does not match the authenticated user's ID.")

        if (userDTO == null || (userDTO.name == null && userDTO.email == null)) {
            return ResponseEntity.badRequest().body("Invalid input: At least one field must be provided.")
        }

        return try {
            val updatedUser = userService.update(id, userDTO)
            ResponseEntity.ok(updatedUser)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(409).body(e.message)
        }
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Any> {
        if (user.role != Role.ADMIN && id != user.id) throw AccessDeniedException("Access denied: You cannot delete another user.")

        if (!userService.isExist(id)) throw IllegalArgumentException("User not found with id: $id")

        userService.delete(id)
        return ResponseEntity.noContent().build()
    }
}