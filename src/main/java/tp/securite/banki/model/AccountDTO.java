package tp.securite.banki.model;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    // null -> new account to be created
    // not null -> retrieving an existing account
    private UUID id;

    @NotNull
    private Double balance;

    @NotNull
    private AccountStatus status;

    @NotNull
    private UUID ownerId; // TODO: keep or remove

}
