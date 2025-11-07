package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class TestRunnerShellController {
    private final TestRunnerService testRunnerService;

    private final LocalizedIOService localizedIOService;

    @ShellMethod(value = "Run test command", key = { "test", "t", "start", "s" })
    public void runTest() {
        try {
            testRunnerService.run();
        } catch (QuestionReadException exception) {
            localizedIOService.printLineLocalized("ShellError.read.questions");
        } catch (Throwable unknown) {
            localizedIOService.printLineLocalized("ShellError.unknown");
        }
    }
}
