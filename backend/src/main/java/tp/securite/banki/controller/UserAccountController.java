package tp.securite.banki.controller;

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

import java.util.UUID;

@RestController
@RequestMapping("user-account")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

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
