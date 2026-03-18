package ru.otus.hw.hw13.services.auth;

public interface RegisterService {
    void registerUser(String username, String password, String firstName, String lastName, String role);
}
