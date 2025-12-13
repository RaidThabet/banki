package tp.securite.banki.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {

    @org.hibernate.validator.constraints.UUID
    private UUID id;

    @NotNull
    private TransactionType type;

    @NotNull
    private Double amount;

    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    @NotNull
    private UUID accountId;

}
