package corp.phonebook.service

interface UserAuditService {
    fun saveRegister(userEmail: String)
    fun saveDelete(userId: Long, userEmail: String)
}