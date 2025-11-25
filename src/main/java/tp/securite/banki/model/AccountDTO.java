package tp.securite.banki.model;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AccountDTO {

    private UUID id;

    @NotNull
    private Double balance;

    @NotNull
    private AccountStatus status;

    @NotNull
    private UUID ownerId;

}
