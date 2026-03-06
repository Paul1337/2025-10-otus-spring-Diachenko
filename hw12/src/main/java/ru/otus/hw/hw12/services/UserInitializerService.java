package ru.otus.hw.hw12.services;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInitializerService {
    private final RegisterService registerService;

    @EventListener(ApplicationReadyEvent.class)
    public void initUsers() {
        registerService.registerUser("user", "password", "John", "Doe");
    }
}
