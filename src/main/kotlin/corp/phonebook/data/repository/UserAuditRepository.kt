package corp.phonebook.data.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import corp.phonebook.data.entity.UserAudit

@Repository
interface UserAuditRepository : JpaRepository<UserAudit, Long>