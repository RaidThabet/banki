package tp.securite.banki.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.securite.banki.domain.Beneficiary;
import tp.securite.banki.domain.User;

import java.util.List;
import java.util.UUID;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, UUID> {
    List<Beneficiary> findByOwnerUser(User ownerUser);
}
