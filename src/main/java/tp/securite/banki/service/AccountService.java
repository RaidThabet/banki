package tp.securite.banki.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tp.securite.banki.domain.Account;
import tp.securite.banki.domain.User;
import tp.securite.banki.events.BeforeDeleteAccount;
import tp.securite.banki.model.AccountDTO;
import tp.securite.banki.repos.AccountRepository;
import tp.securite.banki.repos.UserRepository;
import tp.securite.banki.util.CustomCollectors;
import tp.securite.banki.util.NotFoundException;


@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    public AccountService(final AccountRepository accountRepository,
            final UserRepository userRepository, final ApplicationEventPublisher publisher) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.publisher = publisher;
    }

    public List<AccountDTO> findAll() {
        final List<Account> accounts = accountRepository.findAll(Sort.by("id"));
        return accounts.stream()
                .map(account -> mapToDTO(account, new AccountDTO()))
                .toList();
    }

    public AccountDTO get(final UUID id) {
        return accountRepository.findById(id)
                .map(account -> mapToDTO(account, new AccountDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final AccountDTO accountDTO) {
        final Account account = new Account();
        mapToEntity(accountDTO, account);
        return accountRepository.save(account).getId();
    }

    public void update(final UUID id, final AccountDTO accountDTO) {
        final Account account = accountRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(accountDTO, account);
        accountRepository.save(account);
    }

    public void delete(final UUID id) {
        final Account account = accountRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteAccount(id));
        accountRepository.delete(account);
    }

    private AccountDTO mapToDTO(final Account account, final AccountDTO accountDTO) {
        accountDTO.setId(account.getId());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setStatus(account.getStatus());
        accountDTO.setOwnerId(account.getOwnerId() == null ? null : account.getOwnerId().getId());
        return accountDTO;
    }

    private Account mapToEntity(final AccountDTO accountDTO, final Account account) {
        account.setBalance(accountDTO.getBalance());
        account.setStatus(accountDTO.getStatus());
        final User ownerId = accountDTO.getOwnerId() == null ? null : userRepository.findById(accountDTO.getOwnerId())
                .orElseThrow(() -> new NotFoundException("ownerId not found"));
        account.setOwnerId(ownerId);
        return account;
    }

    public Map<UUID, UUID> getAccountValues() {
        return accountRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Account::getId, Account::getId));
    }

}
