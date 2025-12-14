package tp.securite.banki.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryDTO {

    private UUID id;

    private String name;

    private String bankName;

}
