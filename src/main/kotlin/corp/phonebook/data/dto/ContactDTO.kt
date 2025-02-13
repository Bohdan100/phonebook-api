package corp.phonebook.data.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ContactDTO(
    @field:NotBlank(message = "Name cannot be blank")
    @field:Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    val name: String,

    @field:NotBlank(message = "Number cannot be blank")
    @field:Pattern(regexp = "^\\s*\\+?\\s*[\\d\\s]{3,15}\\s*$", message = "Invalid phone number format")
    val number: String,

    @field:NotNull(message = "Phonebook ID cannot be null")
    val phonebookId: Long
)