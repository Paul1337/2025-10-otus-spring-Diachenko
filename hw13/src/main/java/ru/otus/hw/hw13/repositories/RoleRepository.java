package ru.otus.hw.hw13.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.hw13.models.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
