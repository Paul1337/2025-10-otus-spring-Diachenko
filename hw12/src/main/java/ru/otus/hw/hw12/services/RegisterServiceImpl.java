package ru.otus.hw.hw12.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw12.models.User;
import ru.otus.hw.hw12.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(String username, String password, String firstName, String lastName) {
        var passwordHash = passwordEncoder.encode(password);
        var createdUser = new User(0, username, passwordHash, firstName, lastName);
        userRepository.save(createdUser);
    }
}
