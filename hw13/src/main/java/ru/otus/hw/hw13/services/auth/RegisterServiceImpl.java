package ru.otus.hw.hw13.services.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw13.exceptions.EntityNotFoundException;
import ru.otus.hw.hw13.models.User;
import ru.otus.hw.hw13.repositories.RoleRepository;
import ru.otus.hw.hw13.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(String username, String password, String firstName, String lastName, String role) {
        var passwordHash = passwordEncoder.encode(password);
        var roleUser = roleRepository.findByName(role).orElseThrow(
                () -> new EntityNotFoundException("Role %s not found".formatted(role))
        );

        var createdUser = new User(0, username, passwordHash, firstName, lastName, roleUser);
        userRepository.save(createdUser);
    }
}
