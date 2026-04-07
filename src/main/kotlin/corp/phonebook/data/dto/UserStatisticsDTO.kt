package corp.phonebook.data.dto

import java.io.Serializable

data class UserStatisticsDTO(
    val totalUsers: Long,
    val uniqueEmails: Long,
    val minNameLength: Double,
    val maxNameLength: Double,
    val avgNameLength: Double,
    val adminCount: Long,
    val userCount: Long,
    val activeSessions: Long
) : Serializable