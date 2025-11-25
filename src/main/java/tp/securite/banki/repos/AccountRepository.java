package tp.securite.banki.repos;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tp.securite.banki.domain.Account;


public interface AccountRepository extends JpaRepository<Account, UUID> {
}
