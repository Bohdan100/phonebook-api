package corp.phonebook.service

import org.springframework.data.domain.Pageable
import corp.phonebook.common.dto.PageResponse
import corp.phonebook.data.entity.User
import corp.phonebook.data.dto.UserDTO
import corp.phonebook.data.dto.UsersByRoleDTO
import corp.phonebook.data.dto.UserStatisticsDTO

interface UserService {
    fun getAllOwners(pageable: Pageable): PageResponse<User>
    fun getByName(name: String, pageable: Pageable): PageResponse<User>
    fun getById(id: Long): User
    fun update(id: Long, userDTO: UserDTO): User
    fun delete(id: Long)
    fun getTotalUsersCount(): Long
    fun getActiveSessionsCount(): Long
    fun getUsersCountByRole(): List<UsersByRoleDTO>
    fun getUserStatistics(): UserStatisticsDTO
}