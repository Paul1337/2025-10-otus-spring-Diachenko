package ru.otus.hw.hw13.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.hw.hw13.models.User;
import ru.otus.hw.hw13.repositories.UserRepository;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public final class SecurityUtils {
    private final UserRepository userRepository;

    public User findCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AppUserDetails appUserDetails) {
            Supplier<RuntimeException> fallbackSupplier =
                    () -> new IllegalStateException("User with id %d not found".formatted(appUserDetails.getId()));
            return userRepository.findById(appUserDetails.getId()).orElseThrow(fallbackSupplier);
        }

        throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
    }

    public boolean hasRole(String role) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}