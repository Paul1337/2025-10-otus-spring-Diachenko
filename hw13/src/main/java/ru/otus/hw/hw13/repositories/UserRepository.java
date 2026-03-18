package ru.otus.hw.hw13.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.hw13.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
