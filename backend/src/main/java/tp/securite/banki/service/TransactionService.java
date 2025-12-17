package tp.securite.banki.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tp.securite.banki.domain.Account;
import tp.securite.banki.domain.AuditLog;
import tp.securite.banki.domain.Transaction;
import tp.securite.banki.domain.User;
import tp.securite.banki.exceptions.BusinessException;
import tp.securite.banki.exceptions.ErrorCode;
import tp.securite.banki.model.AccountStatus;
import tp.securite.banki.model.TransactionStatus;
import tp.securite.banki.model.TransactionType;
import tp.securite.banki.repos.AccountRepository;
import tp.securite.banki.repos.AuditLogRepository;
import tp.securite.banki.repos.TransactionRepository;
import tp.securite.banki.util.IpAddressExtractor;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final IpAddressExtractor ipAddressExtractor;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AuditLogRepository auditLogRepository;
    private final EmailService emailService;

    public List<Transaction> listTransactionsForAccount( UUID userID, UUID accountId) {
        Account account = accountRepository.findAccountsByIdAndOwner_Id(accountId, userID)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_ACCOUNT_NOT_FOUND, accountId, userID));

        if (account.getStatus().equals(AccountStatus.LOCKED)) {
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKED, accountId);
        }
        return transactionRepository.findByAccount_Id(accountId);
    }

    @Transactional
    public Transaction createTransactionForUser(
            UUID accountId,
            UUID beneficiaryId,
            double amount,
            HttpServletRequest request
    ) {
        Account userAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, accountId));
        if (userAccount.getStatus().equals(AccountStatus.LOCKED)){
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKED, userAccount.getId());
        }

        Account beneficiaryAccount = accountRepository.findById(beneficiaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, beneficiaryId));
        if (beneficiaryAccount.getStatus().equals(AccountStatus.LOCKED)){
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKED, beneficiaryAccount.getId());
        }

        double currentBalance = userAccount.getBalance();

        if (currentBalance < amount) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_FUNDS, accountId);
        }

        double beneficiaryCurrentBalance = beneficiaryAccount.getBalance();

        userAccount.setBalance(currentBalance - amount);
        beneficiaryAccount.setBalance(beneficiaryCurrentBalance + amount);

        Transaction debitTransaction = new Transaction();
        debitTransaction.setType(TransactionType.DEBIT);
        debitTransaction.setAmount(amount);
        debitTransaction.setAccount(userAccount);
        debitTransaction.setStatus(TransactionStatus.PENDING);

        Transaction creditTransaction = new Transaction();
        creditTransaction.setType(TransactionType.CREDIT);
        creditTransaction.setAmount(amount);
        creditTransaction.setAccount(beneficiaryAccount);
        creditTransaction.setStatus(TransactionStatus.PENDING);

        accountRepository.saveAll(List.of(userAccount, beneficiaryAccount));
        transactionRepository.save(creditTransaction);

        Transaction createdTransaction = transactionRepository.save(debitTransaction);

        User owner = userAccount.getOwner();

        String ipAddress = ipAddressExtractor.getClientIpAddress(request);
        String auditLogMessage = "User with id %s made a new transaction".formatted(owner.getId());

        AuditLog auditLog = AuditLog.builder()
                .userId(owner)
                .action("CREATE_TRANSACTION")
                .ipAddress(ipAddress)
                .details(auditLogMessage)
                .build();

        auditLogRepository.save(auditLog);

        emailService.sendEmail(
                owner.getEmail(),
                "Transaction Created",
                "Your transaction of " + amount + " to " + beneficiaryId + " was successful."
        );

        return createdTransaction;
    }

    @Scheduled(fixedDelay = 60000) // run every 60 seconds
    public void updateTransactionStatus() {
        Random random = new Random();
        List<Transaction> pendingTransactions = transactionRepository.findByStatus(TransactionStatus.PENDING);

        pendingTransactions.forEach(transaction -> {
            TransactionStatus randomStatus = random.nextBoolean() ?
                    TransactionStatus.SUCCESS :
                    TransactionStatus.FAILED;
            transaction.setStatus(randomStatus);
        });
        transactionRepository.saveAll(pendingTransactions);
    }
}
