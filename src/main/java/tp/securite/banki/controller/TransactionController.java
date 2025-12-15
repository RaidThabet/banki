package tp.securite.banki.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tp.securite.banki.domain.Transaction;
import tp.securite.banki.model.CreateTransactionDTO;
import tp.securite.banki.model.TransactionDTO;
import tp.securite.banki.service.TransactionService;
import tp.securite.banki.swagger.TransactionControllerResponses;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("accounts/{account_id}/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "API endpoints for managing transactions within an account")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @Operation(
        summary = "Get all transactions for an account",
        description = "Retrieve all transactions for a specific account. The user can only access transactions from their own accounts"
    )
    @TransactionControllerResponses.GetTransactionsListResponse
    public ResponseEntity<List<TransactionDTO>> getAll(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("account_id") UUID accountId
    ) throws Exception {
        UUID userId = UUID.fromString(jwt.getSubject());
        List<TransactionDTO> transactionDTOS = transactionService.listTransactionsForAccount(userId, accountId).stream()
                .map(transaction -> TransactionDTO.builder()
                        .id(transaction.getId())
                        .type(transaction.getType())
                        .status(transaction.getStatus())
                        .amount(transaction.getAmount())
                        .accountId(accountId)
                        .build()
                )
                .toList();

        return ResponseEntity.ok(transactionDTOS);
    }

    @PostMapping
    @Operation(
        summary = "Create a new transaction",
        description = "Create a new transaction from one account to a beneficiary. The transaction will initially be in PENDING status"
    )
    @TransactionControllerResponses.CreateTransactionResponse
    public ResponseEntity<TransactionDTO> makeTransaction(
            @PathVariable("account_id") UUID accountId,
            @RequestBody CreateTransactionDTO createTransactionDTO,
            HttpServletRequest request
    ) throws Exception {
        UUID beneficiaryId = createTransactionDTO.getBeneficiaryId();
        double amount = createTransactionDTO.getAmount();

        Transaction createdUserTransaction = transactionService.createTransactionForUser(
                accountId,
                beneficiaryId,
                amount,
                request
        );

        TransactionDTO transactionDTO = TransactionDTO.builder()
                .id(createdUserTransaction.getId())
                .amount(amount)
                .accountId(accountId)
                .type(createdUserTransaction.getType())
                .build();

        return ResponseEntity.ok(transactionDTO);
    }
}
