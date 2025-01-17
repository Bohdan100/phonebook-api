package corp.phonebook.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import corp.phonebook.data.entity.User;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE CONCAT(LOWER(:name), '%')")
    List<User> findByNameStartingWithIgnoreCase(@Param("name") String name);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.id = :id")
    boolean existsUserById(@Param("id") Long id);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.id != :id")
    boolean existsUserByEmailAndNotId(@Param("email") String email, @Param("id") Long id);
}
