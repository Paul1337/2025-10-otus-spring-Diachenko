package ru.otus.hw.hw13.services.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.otus.hw.hw13.services.auth.RegisterService;

@Component
@RequiredArgsConstructor
public class UserInitializerService {
    private final RegisterService registerService;

    @EventListener(ApplicationReadyEvent.class)
    public void initUsers() {
        registerService.registerUser("user", "password", "John", "Doe", "USER");
        registerService.registerUser("admin", "password", "Peter", "Doe", "ADMIN");
        registerService.registerUser("moderator", "password", "Ivan", "Doe", "MODERATOR");
    }
}
