package tp.securite.banki.repos;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tp.securite.banki.domain.Transaction;


public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
