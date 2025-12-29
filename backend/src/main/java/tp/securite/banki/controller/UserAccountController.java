package tp.securite.banki.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tp.securite.banki.domain.User;
import tp.securite.banki.model.UserAccountDTO;
import tp.securite.banki.service.UserAccountService;
import tp.securite.banki.swagger.UserAccountControllerResponses;

import java.util.UUID;

@RestController
@RequestMapping("user-account")
@RequiredArgsConstructor
@Tag(name = "User Accounts", description = "API endpoints for managing user accounts")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @Operation(
            summary = "Update user account informations",
            description = "Update the email and phone number of the authenticated user"
    )
    @UserAccountControllerResponses.UpdateUserAccountResponse
    @PostMapping
    public ResponseEntity<UserAccountDTO> updateUserAccount(
            @RequestBody @Valid UserAccountDTO userAccountDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        User user = userAccountService.updateUserAccountInformations(
                userId,
                userAccountDTO.getPhoneNumber(),
                userAccountDTO.getEmail()
        );

        UserAccountDTO responseDTO = UserAccountDTO.builder()
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();

        return ResponseEntity.ok(responseDTO);
    }

}
