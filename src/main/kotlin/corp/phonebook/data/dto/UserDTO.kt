package corp.phonebook.data.dto

import jakarta.validation.constraints.Size

data class UserDTO(
    @field:Size(min = 1, max = 255, message = "Name must be between 2 and 50 characters")
    val name: String?,

    @field:Size(min = 1, max = 255, message = "Name must be between 2 and 50 characters")
    val email: String?
)