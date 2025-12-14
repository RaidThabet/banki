package tp.securite.banki.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tp.securite.banki.domain.Account;
import tp.securite.banki.model.AccountStatus;


public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findAccountsByOwner_Id(UUID ownerId);

    Optional<Account> findAccountsByIdAndOwner_Id(UUID id, UUID ownerId);

    List<Account> findAllByStatus(AccountStatus status);
}
