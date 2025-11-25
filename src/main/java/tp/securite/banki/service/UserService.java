package tp.securite.banki.service;

import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tp.securite.banki.domain.User;
import tp.securite.banki.repos.UserRepository;
import tp.securite.banki.util.CustomCollectors;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<UUID, String> getUserValues() {
        return userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getFullName));
    }

}
