package tp.securite.banki.swagger;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tp.securite.banki.model.AccountDTO;
import tp.securite.banki.model.UserAccountDTO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class UserAccountControllerResponses {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User account updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserAccountDTO.class)
                    )
            ),
    })
    public @interface UpdateUserAccountResponse {
    }

}
