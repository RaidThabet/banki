package tp.securite.banki.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tp.securite.banki.domain.Account;
import tp.securite.banki.model.AccountDTO;
import tp.securite.banki.service.AccountsService;
import tp.securite.banki.swagger.AccountControllerResponses;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "API endpoints for managing user accounts")
public class AccountController {

    private final AccountsService accountsService;

    @GetMapping
    @Operation(
        summary = "Get all accounts",
        description = "Retrieve all accounts owned by the authenticated user"
    )
   @AccountControllerResponses.GetAccountsListResponse
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
    @Operation(
        summary = "Get account by ID",
        description = "Retrieve a specific account by its ID. The user can only access their own accounts"
    )
    @AccountControllerResponses.GetAccountResponse
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
    @Operation(
        summary = "Create a new account",
        description = "Create a new bank account for the authenticated user"
    )
    @AccountControllerResponses.CreateAccountResponse
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
