package ru.otus.hw.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class InMemoryUserSessionService implements UserSessionService {
    private static final int SESSION_TIME_MS = 30000;

    private String currentUsername = null;

    private Instant sessionStartInstant = null;


    @Override
    public void startNewSession(String username) {
        currentUsername = username;
        sessionStartInstant = Instant.now();
    }

    @Override
    public void clearSession() {
        currentUsername = null;
        sessionStartInstant = null;
    }

    @Override
    public Optional<String> getCurrentUsername() {
        if (sessionStartInstant == null) {
            return Optional.empty();
        }
        if (Duration.between(sessionStartInstant, Instant.now()).compareTo(Duration.ofMillis(SESSION_TIME_MS)) < 0) {
            return Optional.ofNullable(currentUsername);
        } else {
            return Optional.empty();
        }
    }
}
