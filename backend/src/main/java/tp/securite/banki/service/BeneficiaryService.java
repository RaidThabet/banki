package tp.securite.banki.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tp.securite.banki.domain.AuditLog;
import tp.securite.banki.domain.Beneficiary;
import tp.securite.banki.domain.User;
import tp.securite.banki.exceptions.BusinessException;
import tp.securite.banki.exceptions.ErrorCode;
import tp.securite.banki.repos.AccountRepository;
import tp.securite.banki.repos.AuditLogRepository;
import tp.securite.banki.repos.BeneficiaryRepository;
import tp.securite.banki.repos.UserRepository;
import tp.securite.banki.util.IpAddressExtractor;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final IpAddressExtractor ipAddressExtractor;
    private final BeneficiaryRepository beneficiaryRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AuditLogRepository auditLogRepository;
    private final EmailService emailService;

    @Transactional
    public Beneficiary createBeneficiary(
            UUID userId,
            String name,
            String bankName,
            UUID accountId,
            HttpServletRequest request
    ) {
        User ownerUser = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
        accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_ACCOUNT_NOT_FOUND, accountId, userId));

        Beneficiary beneficiary = new Beneficiary();
        if (bankName.equals("Banki")) {
            beneficiary.setId(accountId);
        } else {
            beneficiary.setId(UUID.randomUUID());
        }
        beneficiary.setOwnerUser(ownerUser);
        beneficiary.setName(name);
        beneficiary.setBankName(bankName);

        Beneficiary createdBeneficiary = beneficiaryRepository.save(beneficiary);

        String ipAddress = ipAddressExtractor.getClientIpAddress(request);
        String auditLogMessage = "User with id %s created a new beneficiary".formatted(userId);

        AuditLog auditLog = AuditLog.builder()
                .userId(ownerUser)
                .action("CREATE_BENEFICIARY")
                .ipAddress(ipAddress)
                .details(auditLogMessage)
                .build();

        auditLogRepository.save(auditLog);

        emailService.sendEmail(
                ownerUser.getEmail(),
                "Beneficiary Created",
                "Your beneficiary creation was successful"
        );

        return createdBeneficiary;
    }

    public List<Beneficiary> getBeneficiaries(UUID userId) {
        User ownerUser = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        return beneficiaryRepository.findByOwnerUser(ownerUser);
    }
}
