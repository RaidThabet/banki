package tp.securite.banki.service;

import java.util.List;
import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tp.securite.banki.domain.Account;
import tp.securite.banki.domain.Transaction;
import tp.securite.banki.events.BeforeDeleteAccount;
import tp.securite.banki.model.TransactionDTO;
import tp.securite.banki.repos.AccountRepository;
import tp.securite.banki.repos.TransactionRepository;
import tp.securite.banki.util.NotFoundException;
import tp.securite.banki.util.ReferencedException;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(final TransactionRepository transactionRepository,
            final AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<TransactionDTO> findAll() {
        final List<Transaction> transactions = transactionRepository.findAll(Sort.by("id"));
        return transactions.stream()
                .map(transaction -> mapToDTO(transaction, new TransactionDTO()))
                .toList();
    }

    public TransactionDTO get(final UUID id) {
        return transactionRepository.findById(id)
                .map(transaction -> mapToDTO(transaction, new TransactionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final TransactionDTO transactionDTO) {
        final Transaction transaction = new Transaction();
        mapToEntity(transactionDTO, transaction);
        return transactionRepository.save(transaction).getId();
    }

    public void update(final UUID id, final TransactionDTO transactionDTO) {
        final Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(transactionDTO, transaction);
        transactionRepository.save(transaction);
    }

    public void delete(final UUID id) {
        final Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        transactionRepository.delete(transaction);
    }

    private TransactionDTO mapToDTO(final Transaction transaction,
            final TransactionDTO transactionDTO) {
        transactionDTO.setId(transaction.getId());
        transactionDTO.setType(transaction.getType());
        transactionDTO.setFromAccountID(transaction.getFromAccountID());
        transactionDTO.setToAccountID(transaction.getToAccountID());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setStatus(transaction.getStatus());
        transactionDTO.setAccountId(transaction.getAccountId() == null ? null : transaction.getAccountId().getId());
        return transactionDTO;
    }

    private Transaction mapToEntity(final TransactionDTO transactionDTO,
            final Transaction transaction) {
        transaction.setType(transactionDTO.getType());
        transaction.setFromAccountID(transactionDTO.getFromAccountID());
        transaction.setToAccountID(transactionDTO.getToAccountID());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setStatus(transactionDTO.getStatus());
        final Account accountId = transactionDTO.getAccountId() == null ? null : accountRepository.findById(transactionDTO.getAccountId())
                .orElseThrow(() -> new NotFoundException("accountId not found"));
        transaction.setAccountId(accountId);
        return transaction;
    }

    @EventListener(BeforeDeleteAccount.class)
    public void on(final BeforeDeleteAccount event) {
        final ReferencedException referencedException = new ReferencedException();
        final Transaction accountIdTransaction = transactionRepository.findFirstByAccountIdId(event.getId());
        if (accountIdTransaction != null) {
            referencedException.setKey("account.transaction.accountId.referenced");
            referencedException.addParam(accountIdTransaction.getId());
            throw referencedException;
        }
    }

}
