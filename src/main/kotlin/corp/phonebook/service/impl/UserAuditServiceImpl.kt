package corp.phonebook.service.impl

import org.springframework.stereotype.Service
import corp.phonebook.service.UserAuditService
import corp.phonebook.data.repository.UserAuditRepository
import corp.phonebook.data.entity.UserAudit

@Service
class UserAuditServiceImpl(
    private val userAuditRepository: UserAuditRepository
) : UserAuditService {

    override fun saveRegister(userEmail: String) {
        val audit = UserAudit(
            operation = "REGISTER",
            userEmail = userEmail
        )
        userAuditRepository.save(audit)
    }

    override fun saveDelete(userId: Long, userEmail: String) {
        val audit = UserAudit(
            operation = "DELETE",
            userId = userId,
            userEmail = userEmail
        )
        userAuditRepository.save(audit)
    }
}