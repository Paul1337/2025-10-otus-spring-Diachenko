package ru.otus.hw.service;

import java.util.Optional;

public interface UserSessionService {
    void startNewSession(String username);

    void clearSession();

    Optional<String> getCurrentUsername();
}
