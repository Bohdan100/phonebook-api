package corp.phonebook.service

import org.springframework.stereotype.Service
import corp.phonebook.data.repository.UserRepository
import corp.phonebook.data.repository.PhoneBookRepository
import corp.phonebook.data.dto.UserDTO
import corp.phonebook.data.entity.User
import corp.phonebook.data.entity.Contact

@Service
class UserService(
    private val userRepository: UserRepository,
    private val phoneBookRepository: PhoneBookRepository
) {

    fun getAllOwners(): List<User> {
        val phoneBooks = phoneBookRepository.findAll()
        val owners = mutableListOf<User>()
        for (phoneBook in phoneBooks) {
            val user = phoneBook.owner
            if (user != null) {
                owners.add(user)
            }
        }
        return owners
    }

    fun getById(id: Long): User? = userRepository.findUserById(id)

    fun getByName(name: String): List<User> = userRepository.findByNameStartingWithIgnoreCase(name)

    fun update(id: Long, userDTO: UserDTO): User {
        val userFromDB = userRepository.findUserById(id)
            ?: throw IllegalArgumentException("User with ID $id not found.")

        if (userDTO.email != null && userDTO.email != userFromDB.email) {
            val emailExists = userRepository.existsUserByEmailAndNotId(userDTO.email, id)
            if (emailExists) {
                throw IllegalArgumentException("Email already in use by another user.")
            }
            userFromDB.email = userDTO.email
        }

        if (userDTO.name != null) {
            userFromDB.name = userDTO.name
        }

        return userRepository.save(userFromDB)
    }

    fun delete(id: Long) {
        userRepository.deleteById(id)
    }

    fun isExist(id: Long): Boolean {
        return userRepository.existsUserById(id)
    }

    fun getAllUserContacts(id: Long): List<Contact> {
        val phoneBook = phoneBookRepository.findPhoneBookByOwnerId(id)
        return phoneBook.contacts
    }
}