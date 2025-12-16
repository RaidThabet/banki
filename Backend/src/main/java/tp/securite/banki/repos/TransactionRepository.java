package tp.securite.banki.repos;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tp.securite.banki.domain.Transaction;
import tp.securite.banki.model.TransactionStatus;


public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccount_Id(UUID accountId);

    List<Transaction> findByStatus(TransactionStatus status);
}
