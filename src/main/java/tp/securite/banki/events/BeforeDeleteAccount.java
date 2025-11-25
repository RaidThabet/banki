package tp.securite.banki.events;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class BeforeDeleteAccount {

    private UUID id;

}
