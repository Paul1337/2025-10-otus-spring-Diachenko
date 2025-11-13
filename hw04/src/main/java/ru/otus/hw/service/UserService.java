package ru.otus.hw.service;

import java.util.Optional;

public interface UserService {
    void registerUser(String username);

    void loginUser(String username);

    void logout();

    Optional<String> getCurrentLoggedUser();
}
