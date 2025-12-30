package tp.securite.banki.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tp.securite.banki.domain.User;
import tp.securite.banki.exceptions.BusinessException;
import tp.securite.banki.exceptions.ErrorCode;
import tp.securite.banki.repos.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;

    public User getUserAccountInformations(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
    }

    public User updateUserAccountInformations(
            UUID userId,
            String newPhoneNumber,
            String newEmail
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        user.setPhoneNumber(newPhoneNumber);
        user.setEmail(newEmail);

        User savedUser = userRepository.save(user);
        keycloakAdminService.updateUser(userId, newPhoneNumber, newEmail);

        return savedUser;
    }
}
