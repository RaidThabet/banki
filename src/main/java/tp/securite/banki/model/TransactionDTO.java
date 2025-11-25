package tp.securite.banki.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransactionDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String type;

    @NotNull
    private UUID fromAccountID;

    @NotNull
    private UUID toAccountID;

    @NotNull
    private Double amount;

    @NotNull
    private TransactionStatus status;

    @NotNull
    private UUID accountId;

}
