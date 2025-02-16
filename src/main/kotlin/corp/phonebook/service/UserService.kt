package corp.phonebook.service

import org.springframework.stereotype.Service
import corp.phonebook.data.repository.UserRepository
import corp.phonebook.data.repository.PhoneBookRepository
import corp.phonebook.data.dto.UserDTO
import corp.phonebook.data.entity.User
import corp.phonebook.errors.EmailAlreadyExistsException
import corp.phonebook.errors.ResourceNotFoundException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val phoneBookRepository: PhoneBookRepository
) {
    fun getAllOwners(): List<User> = phoneBookRepository.findAllOwners()

    fun getByName(name: String): List<User> =
        userRepository.findByNameStartingWithIgnoreCase(name)

    fun getById(id: Long): User =
        userRepository.findUserById(id).orElseThrow { ResourceNotFoundException("User not found.") }

    fun update(id: Long, userDTO: UserDTO): User {
        if (userDTO.name == null && userDTO.email == null)
            throw IllegalArgumentException("Invalid input: At least one field must be provided.")

        val userFromDB = userRepository.findUserById(id)
            .orElseThrow { ResourceNotFoundException("User not found.") }

        if (userDTO.email != null && userDTO.email != userFromDB.email) {
            val emailExists = userRepository.existsUserByEmailAndNotId(userDTO.email, id)
            if (emailExists) throw EmailAlreadyExistsException("Email already in use by another user.")

            userFromDB.email = userDTO.email
        }

        if (userDTO.name != null) userFromDB.name = userDTO.name

        return userRepository.save(userFromDB)
    }

    fun delete(id: Long) {
        if (!userRepository.existsUserById(id))
            throw ResourceNotFoundException("User not found with id: $id")

        userRepository.deleteById(id)
    }
}