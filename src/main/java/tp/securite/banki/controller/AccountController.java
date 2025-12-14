package tp.securite.banki.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tp.securite.banki.domain.Account;
import tp.securite.banki.model.AccountDTO;
import tp.securite.banki.service.AccountsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountsService accountsService;

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAll(
            @AuthenticationPrincipal Jwt jwt
            ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        List<AccountDTO> accountDTOS = accountsService.listAccounts(userId).stream()
                .map(account -> AccountDTO.builder()
                        .id(account.getId())
                        .balance(account.getBalance())
                        .status(account.getStatus())
                        .ownerId(account.getOwner().getId())
                        .build()
                )
                .toList();

        return ResponseEntity.ok(accountDTOS);
    }

    @GetMapping("{account_id}")
    public ResponseEntity<AccountDTO> getAccount(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("account_id") UUID accountId
    ) throws Exception {
        UUID userId = UUID.fromString(jwt.getSubject());
        Account account = accountsService.getAccount(userId, accountId);
        AccountDTO accountDTO = AccountDTO.builder()
                .id(accountId)
                .balance(account.getBalance())
                .status(account.getStatus())
                .ownerId(userId)
                .build();

        return ResponseEntity.ok(accountDTO);
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody AccountDTO accountDTO
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        Account createdAccount = accountsService.createAccountForUser(userId, accountDTO);
        AccountDTO createdAccountDTO = AccountDTO.builder()
                .id(createdAccount.getId())
                .balance(createdAccount.getBalance())
                .status(createdAccount.getStatus())
                .ownerId(createdAccount.getOwner().getId())
                .build();

        return ResponseEntity.ok(createdAccountDTO);
    }
}
