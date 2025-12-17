package tp.securite.banki.swagger;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tp.securite.banki.model.AccountDTO;
import tp.securite.banki.model.BeneficiaryDTO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class BeneficiaryControllerResponses {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Beneficiaries retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BeneficiaryDTO.class), minItems = 3, maxItems = 3)
                    )
            ),
    })
    public @interface GetBeneficiariesListResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Beneficiary created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BeneficiaryDTO.class)
                    )
            ),
    })
    public @interface CreateBeneficiaryResponse {
    }
}
