package tp.securite.banki.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tp.securite.banki.domain.Account;
import tp.securite.banki.domain.Transaction;
import tp.securite.banki.exceptions.BusinessException;
import tp.securite.banki.exceptions.ErrorCode;
import tp.securite.banki.model.AccountStatus;
import tp.securite.banki.model.TransactionStatus;
import tp.securite.banki.model.TransactionType;
import tp.securite.banki.repos.AccountRepository;
import tp.securite.banki.repos.TransactionRepository;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

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
            double amount
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

        return transactionRepository.save(debitTransaction);
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
