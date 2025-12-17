package tp.securite.banki.repos;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tp.securite.banki.domain.User;


public interface UserRepository extends JpaRepository<User, UUID> {
}
