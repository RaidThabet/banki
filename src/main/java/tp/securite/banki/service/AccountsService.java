package tp.securite.banki.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tp.securite.banki.domain.Account;
import tp.securite.banki.domain.User;
import tp.securite.banki.exceptions.BusinessException;
import tp.securite.banki.exceptions.ErrorCode;
import tp.securite.banki.model.AccountDTO;
import tp.securite.banki.model.AccountStatus;
import tp.securite.banki.repos.AccountRepository;
import tp.securite.banki.repos.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountsService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public List<Account> listAccounts(UUID ownerId) {

        return accountRepository.findAccountsByOwner_Id(ownerId);
    }

    public Account createAccountForUser(UUID ownerId, AccountDTO accountDTO) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, ownerId));

        Account account = new Account();
        account.setBalance(0D);
        account.setStatus(AccountStatus.LOCKED); // initially, the new account is locked
        account.setOwner(owner);

        return accountRepository.save(account);
    }

    public Account getAccount(UUID ownerId, UUID accountId) {

        return accountRepository.findAccountsByIdAndOwner_Id(accountId, ownerId)
                .orElseThrow();
    }

}
