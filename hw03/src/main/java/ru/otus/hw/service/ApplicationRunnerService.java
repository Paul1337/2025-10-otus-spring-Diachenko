package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ApplicationRunnerService implements ApplicationRunner {
    private final TestRunnerService testRunnerService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        testRunnerService.run();
    }
}
