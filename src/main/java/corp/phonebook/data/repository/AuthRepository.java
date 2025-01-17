package corp.phonebook.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import corp.phonebook.data.entity.User;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByToken(String token);

    Optional<User> findBySessionId(String sessionId);
}
