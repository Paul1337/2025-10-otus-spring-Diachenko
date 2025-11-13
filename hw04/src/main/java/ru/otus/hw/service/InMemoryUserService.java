package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.UserNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InMemoryUserService implements UserService {
    private Set<String> registeredUsernames = new HashSet<>();

    private final UserSessionService userSessionService;

    @Override
    public void registerUser(String username) {
        registeredUsernames.add(username);
    }

    @Override
    public void loginUser(String username) {
        if (!registeredUsernames.contains(username)) {
            throw new UserNotFoundException("User not registered");
        }
        userSessionService.startNewSession(username);
    }

    @Override
    public void logout() {
        userSessionService.clearSession();
    }

    @Override
    public Optional<String> getCurrentLoggedUser() {
        return userSessionService.getCurrentUsername();
    }

}
