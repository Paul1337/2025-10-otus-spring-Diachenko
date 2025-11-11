package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.command.annotation.CommandAvailability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.exceptions.UserNotFoundException;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;
import ru.otus.hw.service.UserService;

@ShellComponent
@RequiredArgsConstructor
public class TestRunnerShellController {
    private final TestRunnerService testRunnerService;

    private final LocalizedIOService localizedIOService;

    private final UserService userService;

    @ShellMethod(value = "Run test command", key = { "test" })
    @ShellMethodAvailability(value = "isRunTestAvailable")
    public void runTest() {
        try {
            testRunnerService.run();
        } catch (QuestionReadException exception) {
            localizedIOService.printLineLocalized("Shell.test.error.read.questions");
        }
    }

    private Availability isRunTestAvailable() {
        return userService.getCurrentLoggedUser().isPresent()
                ? Availability.available()
                : Availability.unavailable(localizedIOService.getMessage("Shell.test.error.login.required"));
    }

    @ShellMethod(value = "Register command", key = { "register", "reg" })
    public void register(String username) {
        userService.registerUser(username);
        localizedIOService.printLineLocalized("Shell.register.ok");
    }

    @ShellMethod(value = "Login command", key = { "login", "log" })
    public void login(String username) {
        try {
            userService.loginUser(username);
            localizedIOService.printLineLocalized("Shell.login.ok");
        } catch (UserNotFoundException exception) {
            localizedIOService.printLineLocalized("Shell.login.user.not.found");
        }
    }

    @ShellMethod(value = "Logout command", key = { "logout" })
    public void logout() {
        userService.logout();
        localizedIOService.printLineLocalized("Shell.logout.ok");
    }

    @ShellMethod(value = "Get current user", key = { "whoami", "me" })
    public String getCurrentUser() {
        var currentUsername = userService.getCurrentLoggedUser();
        return currentUsername.orElseGet(() -> localizedIOService.getMessage("Shell.whoami.no.current.user"));
    }
}
