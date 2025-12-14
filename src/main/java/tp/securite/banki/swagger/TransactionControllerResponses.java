package tp.securite.banki.swagger;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tp.securite.banki.model.TransactionDTO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class TransactionControllerResponses {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transactions retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TransactionDTO.class), minItems = 3, maxItems = 3)
                    )
            ),
    })
    public @interface GetTransactionsListResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Transaction created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class)
                    )
            ),
    })
    public @interface CreateTransactionResponse {
    }
}
