package tp.securite.banki.model;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionDTO {

    private UUID beneficiaryId;

    @Positive(message = "Amount should be positive")
    private double amount;

}
