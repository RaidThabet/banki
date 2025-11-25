package tp.securite.banki.repos;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tp.securite.banki.domain.AuditLog;


public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
}
