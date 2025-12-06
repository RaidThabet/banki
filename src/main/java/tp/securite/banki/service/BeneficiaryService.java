package tp.securite.banki.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tp.securite.banki.domain.Beneficiary;
import tp.securite.banki.domain.User;
import tp.securite.banki.exceptions.BusinessException;
import tp.securite.banki.exceptions.ErrorCode;
import tp.securite.banki.repos.BeneficiaryRepository;
import tp.securite.banki.repos.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final UserRepository userRepository;

    @Transactional
    public Beneficiary createBeneficiary(UUID userId, String name, String bankName) {
        User ownerUser = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setOwnerUser(ownerUser);
        beneficiary.setName(name);
        beneficiary.setBankName(bankName);

        return beneficiaryRepository.save(beneficiary);
    }

    public List<Beneficiary> getBeneficiaries(UUID userId) {
        User ownerUser = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        return beneficiaryRepository.findByOwnerUser(ownerUser);
    }
}
