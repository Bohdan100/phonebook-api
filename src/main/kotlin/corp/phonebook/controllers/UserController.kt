package corp.phonebook.controllers

import org.springframework.web.bind.annotation.*
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.access.AccessDeniedException
import org.springframework.http.ResponseEntity

import corp.phonebook.service.UserService
import corp.phonebook.data.dto.UserDTO
import corp.phonebook.data.entity.User
import corp.phonebook.data.entity.Role
import corp.phonebook.constants.Constants.VERSION

@RestController
@RequestMapping("$VERSION/phonebooks/users")
class UserController(
    private val userService: UserService
) {
    @Secured("ROLE_ADMIN")
    @GetMapping("/owners")
    fun getAllOwnersOfPhonebooks(): ResponseEntity<Any> =
        ResponseEntity.ok(userService.getAllOwners())

    @Secured("ROLE_ADMIN")
    @GetMapping("/names/{name}")
    fun getUserByName(@PathVariable name: String): ResponseEntity<Any> =
        ResponseEntity.ok(userService.getByName(name))

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: User?
    ): ResponseEntity<Any> {
        if (user?.role != Role.ADMIN && id != user?.id)
            throw AccessDeniedException("User ID does not match the authenticated user's ID.")

        return ResponseEntity.ok(userService.getById(id))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody userDTO: UserDTO,
        @AuthenticationPrincipal user: User?
    ): ResponseEntity<Any> {
        if (id != user?.id)
            throw AccessDeniedException("User ID does not match the authenticated user's ID.")

        return ResponseEntity.ok(userService.update(id, userDTO))
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Any> {
        if (user.role != Role.ADMIN && id != user.id)
            throw AccessDeniedException("You cannot delete another user.")

        userService.delete(id)
        return ResponseEntity.noContent().build()
    }
}