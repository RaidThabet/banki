// java
package tp.securite.banki.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakAdminService {

    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloakAdminClient;

    public void updateUser(
            UUID userId,
            String newPhoneNumber,
            String newEmail
    ) {
        UsersResource usersResource = keycloakAdminClient.realm(realm).users();

        UserResource userResource = usersResource.get(userId.toString());

        UserRepresentation user = userResource.toRepresentation();

        user.setEmail(newEmail);

        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes == null) {
            attributes = new java.util.HashMap<>();
        }

        attributes.put("phoneNumber", Collections.singletonList(newPhoneNumber));

        user.setAttributes(attributes);

        userResource.update(user);
    }
}
