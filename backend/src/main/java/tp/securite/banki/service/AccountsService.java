package tp.securite.banki.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tp.securite.banki.domain.Account;
import tp.securite.banki.domain.AuditLog;
import tp.securite.banki.domain.User;
import tp.securite.banki.exceptions.BusinessException;
import tp.securite.banki.exceptions.ErrorCode;
import tp.securite.banki.model.AccountDTO;
import tp.securite.banki.model.AccountStatus;
import tp.securite.banki.repos.AccountRepository;
import tp.securite.banki.repos.AuditLogRepository;
import tp.securite.banki.repos.UserRepository;
import tp.securite.banki.util.IpAddressExtractor;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountsService {

    private final IpAddressExtractor ipAddressExtractor;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final EmailService emailService;

    public List<Account> listAccounts(UUID ownerId) {
        return accountRepository.findAccountsByOwner_Id(ownerId);
    }

    @Transactional
    public Account createAccountForUser(UUID ownerId, AccountDTO accountDTO, HttpServletRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, ownerId));

        Account account = new Account();
        account.setBalance(0D);
        account.setStatus(AccountStatus.LOCKED); // initially, the new account is locked
        account.setOwner(owner);

        Account savedAccount = accountRepository.save(account);

        String ipAddress = ipAddressExtractor.getClientIpAddress(request);
        String auditLogMessage = "User with id %s created a new account with id %s".formatted(owner.getId(), savedAccount.getId());

        AuditLog auditLog = AuditLog.builder()
                .userId(owner)
                .action("CREATE_ACCOUNT")
                .ipAddress(ipAddress)
                .details(auditLogMessage)
                .build();

        auditLogRepository.save(auditLog);

        emailService.sendEmail(
                owner.getEmail(),
                "Account Created",
                "Your new account creation was successful"
        );

        return savedAccount;
    }

    public Account getAccount(UUID ownerId, UUID accountId) {

        Account account = accountRepository.findAccountsByIdAndOwner_Id(accountId, ownerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_ACCOUNT_NOT_FOUND, accountId, ownerId));

        if (account.getStatus().equals(AccountStatus.LOCKED)) {
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKED, accountId);
        }

        return account;
    }

    @Scheduled(fixedDelay = 60000)
    public void activateAccounts() {
        List<Account> lockedAccounts = accountRepository.findAllByStatus(AccountStatus.LOCKED);

        lockedAccounts.forEach(account -> account.setStatus(AccountStatus.ACTIVE));

        accountRepository.saveAll(lockedAccounts);
    }

}
