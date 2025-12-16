package tp.securite.banki.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "100.5")
    private Double balance;

    @NotNull
    private AccountStatus status;

    @NotNull
    private UUID ownerId; // TODO: keep or remove

}
