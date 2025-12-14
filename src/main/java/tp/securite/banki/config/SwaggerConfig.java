package tp.securite.banki.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApiSpec() {
        return new OpenAPI().components(new Components()
                .addSchemas("ErrorResponse", new ObjectSchema()
                        .addProperty("message", new StringSchema().example("Error message"))
                        .addProperty("code", new StringSchema().example("ERROR_CODE"))
                        .addProperty("validationErrors", new ArraySchema().items(
                                new ObjectSchema()
                                        .addProperty("field", new StringSchema().example("fieldName"))
                                        .addProperty("code", new StringSchema().example("CODE"))
                                        .addProperty("message", new StringSchema().example("Validation error message"))))));
    }


    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            Map<String, Object> errorExample = new HashMap<>();
            errorExample.put("message", "Error message");
            errorExample.put("code", "ERROR_CODE");

            Map<String, String> ve = new HashMap<>();
            ve.put("field", "fieldName");
            ve.put("code", "CODE");
            ve.put("message", "Validation error message");

            List<Map<String, String>> veList = new ArrayList<>();
            veList.add(ve);

            errorExample.put("validationErrors", veList);

            operation.getResponses().addApiResponse("4xx/5xx", new ApiResponse()
                    .description("Error")
                    .content(new Content().addMediaType("application/json", new MediaType()
                            .schema(new Schema<>().$ref("ErrorResponse"))
                            .example(errorExample))));
            return operation;
        };
    }

}
