package corp.phonebook.data.dto

import java.io.Serializable

data class ContactStatisticsDTO(
    val totalContacts: Long,
    val uniqueNumbers: Long,
    val minNameLength: Double,
    val maxNameLength: Double,
    val avgNameLength: Double,
    val minNumberLength: Double,
    val maxNumberLength: Double,
    val avgNumberLength: Double
) : Serializable