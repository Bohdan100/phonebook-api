package corp.phonebook.data.dto

import corp.phonebook.data.entity.Role
import java.io.Serializable

data class UsersByRoleDTO(
    val role: Role,
    val count: Long
) : Serializable