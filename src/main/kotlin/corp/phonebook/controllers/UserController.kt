package corp.phonebook.controllers

import org.springframework.web.bind.annotation.*
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.http.ResponseEntity
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

import corp.phonebook.service.UserService
import corp.phonebook.data.dto.UserDTO
import corp.phonebook.data.entity.User
import corp.phonebook.data.entity.Role
import corp.phonebook.data.dto.UsersByRoleDTO
import corp.phonebook.data.dto.UserStatisticsDTO
import corp.phonebook.common.dto.PageResponse
import corp.phonebook.constants.Constants.VERSION
import corp.phonebook.exception.types.AccessDeniedException

@RestController
@RequestMapping("$VERSION/phonebooks/users")
class UserController(
    private val userService: UserService
) {
    // GET /api/v1/phonebooks/users/owners?page=0&size=10&sort=name,asc
    @Secured("ROLE_ADMIN")
    @GetMapping("/owners")
    fun getAllOwnersOfPhonebooks(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "name,asc") sort: String
    ): ResponseEntity<PageResponse<User>> {
        val sortParams = sort.split(",")
        val sortDirection = if (sortParams.size > 1 && sortParams[1].lowercase() == "desc") {
            Sort.Direction.DESC
        } else {
            Sort.Direction.ASC
        }
        val sortField = sortParams[0]
        val pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField))
        val result = userService.getAllOwners(pageable)

        return ResponseEntity.ok(result)
    }

    // GET /api/v1/phonebooks/users/names/{name}?page=0&size=10&sort=name,asc
    @Secured("ROLE_ADMIN")
    @GetMapping("/names/{name}")
    fun getUserByName(
        @PathVariable name: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "name,asc") sort: String
    ): ResponseEntity<PageResponse<User>> {
        val sortParams = sort.split(",")
        val sortDirection = if (sortParams.size > 1 && sortParams[1].lowercase() == "desc") {
            Sort.Direction.DESC
        } else {
            Sort.Direction.ASC
        }
        val sortField = sortParams[0]
        val pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField))
        val result = userService.getByName(name, pageable)

        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: User?
    ): ResponseEntity<Any> {
        if (user?.role != Role.ADMIN && id != user?.id)
            throw AccessDeniedException("User ID does not match the authenticated user's ID.")

        return ResponseEntity.ok(userService.getById(id))
    }

    // GET /api/v1/phonebooks/users/statistics/total
    @Secured("ROLE_ADMIN")
    @GetMapping("/statistics/total")
    fun getTotalUsersCount(): ResponseEntity<Map<String, Long>> {
        val count = userService.getTotalUsersCount()
        return ResponseEntity.ok(mapOf("totalUsers" to count))
    }

    // GET /api/v1/phonebooks/users/statistics/active-sessions
    @Secured("ROLE_ADMIN")
    @GetMapping("/statistics/active-sessions")
    fun getActiveSessionsCount(): ResponseEntity<Map<String, Long>> {
        val count = userService.getActiveSessionsCount()
        return ResponseEntity.ok(mapOf("activeSessions" to count))
    }

    // GET /api/v1/phonebooks/users/statistics/by-role
    @Secured("ROLE_ADMIN")
    @GetMapping("/statistics/by-role")
    fun getUsersCountByRole(): ResponseEntity<List<UsersByRoleDTO>> {
        val statistics = userService.getUsersCountByRole()
        return ResponseEntity.ok(statistics)
    }

    // GET /api/v1/phonebooks/users/statistics/full
    @Secured("ROLE_ADMIN")
    @GetMapping("/statistics/full")
    fun getUserStatistics(): ResponseEntity<UserStatisticsDTO> {
        val statistics = userService.getUserStatistics()
        return ResponseEntity.ok(statistics)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody userDTO: UserDTO,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<Any> {
        if (user.role != Role.ADMIN && id != user.id)
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