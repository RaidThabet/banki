package tp.securite.banki.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {

    private UUID id;

    @NotNull
    private TransactionType type;

    @NotNull
    @Positive(message = "Amount should be positive")
    private Double amount;

    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    @NotNull
    private UUID accountId;

}
