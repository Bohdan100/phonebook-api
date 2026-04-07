package corp.phonebook.service.impl

import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable
import corp.phonebook.service.UserService
import corp.phonebook.service.UserAuditService
import corp.phonebook.data.repository.UserRepository
import corp.phonebook.data.repository.PhoneBookRepository
import corp.phonebook.data.entity.User
import corp.phonebook.data.entity.Role
import corp.phonebook.common.dto.PageResponse
import corp.phonebook.data.dto.UserDTO
import corp.phonebook.data.dto.UsersByRoleDTO
import corp.phonebook.data.dto.UserStatisticsDTO
import corp.phonebook.exception.types.EmailAlreadyExistsException
import corp.phonebook.exception.types.ResourceNotFoundException
import corp.phonebook.exception.types.ValidationException

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val phoneBookRepository: PhoneBookRepository,
    private val userAuditService: UserAuditService
) : UserService {
    override fun getAllOwners(pageable: Pageable): PageResponse<User> {
        val page = phoneBookRepository.findAllOwners(pageable)
        return PageResponse.from(page)
    }

    override fun getByName(name: String, pageable: Pageable): PageResponse<User> {
        val page = userRepository.findByNameStartingWithIgnoreCase(name, pageable)
        return PageResponse.from(page)
    }

    override fun getById(id: Long): User =
        userRepository.findUserById(id).orElseThrow { ResourceNotFoundException("User with ID $id not found.") }

    override fun update(id: Long, userDTO: UserDTO): User {
        if (userDTO.name == null && userDTO.email == null)
            throw ValidationException("At least one field (name or email) must be provided for update.")

        val userFromDB = getById(id)

        if (userDTO.email != null && userDTO.email != userFromDB.email) {
            if (userRepository.existsUserByEmailAndNotId(userDTO.email, id))
                throw EmailAlreadyExistsException("Email ${userDTO.email} is already taken.")
            userFromDB.email = userDTO.email
        }

        userDTO.name?.let { userFromDB.name = it }

        return userRepository.save(userFromDB)
    }

    override fun delete(id: Long) {
        if (!userRepository.existsUserById(id))
            throw ResourceNotFoundException("Cannot delete: User with ID $id not found.")

        val user = getById(id)
        userAuditService.saveDelete(user.id!!, user.email)

        userRepository.deleteById(id)
    }

    override fun getTotalUsersCount(): Long {
        return userRepository.countTotalUsers()
    }

    override fun getActiveSessionsCount(): Long {
        return userRepository.countActiveSessions()
    }

    override fun getUsersCountByRole(): List<UsersByRoleDTO> {
        return userRepository.countUsersByRole().map {
            UsersByRoleDTO(role = it[0] as Role, count = it[1] as Long)
        }
    }

    override fun getUserStatistics(): UserStatisticsDTO {
        return userRepository.getUserStatistics()
    }
}