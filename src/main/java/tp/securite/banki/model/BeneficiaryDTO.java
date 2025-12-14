package tp.securite.banki.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryDTO {

    private UUID id;

    @NotEmpty(message = "Name cannot be empty")
    @NotBlank(message = "Name cannot be blank")
    @Length(min = 3, max = 20, message = "Name should be between {min} and {max} characters")
    private String name;

    @NotEmpty(message = "Bank name cannot be empty")
    @NotBlank(message = "Bank name cannot be blank")
    @Length(min = 3, max = 20, message = "Bank name should be between {min} and {max} characters")
    private String bankName;

}
