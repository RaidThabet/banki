package tp.securite.banki.model;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ErrorResponse {

    private String message;

    private String code;

    private List<ValidationError> validationErrors;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ValidationError {
        private String field;
        private String code;
        private String message;
    }
}
