package tp.securite.banki.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tp.securite.banki.domain.Transaction;
import tp.securite.banki.model.CreateTransactionDTO;
import tp.securite.banki.model.TransactionDTO;
import tp.securite.banki.service.TransactionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("{account_id}/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAll(
            @PathVariable("account_id") UUID accountId
    ) {
        List<TransactionDTO> transactionDTOS = transactionService.listTransactionsForAccount(accountId).stream()
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
    public ResponseEntity<TransactionDTO> makeTransaction(
            @PathVariable("account_id") UUID accountId,
            @RequestBody CreateTransactionDTO createTransactionDTO
    ) throws Exception {
        UUID beneficiaryId = createTransactionDTO.getBeneficiaryId();
        double amount = createTransactionDTO.getAmount();

        Transaction createdUserTransaction = transactionService.createTransactionForUser(
                accountId,
                beneficiaryId,
                amount
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
