package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {
    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionToStringConverter questionToStringConverter;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var questionAsString = questionToStringConverter.convert(question);
            var answerIndex = ioService.readIntForRangeWithPrompt(
                    1, question.answers().size(), questionAsString, "Answer is not in the required range"
            ) - 1;
            var isAnswerValid = question.answers().get(answerIndex).isCorrect();
            if (isAnswerValid) {
                ioService.printLine("Yep");
            } else {
                var rightAnswer = question.answers().stream().filter(Answer::isCorrect).findAny().orElseThrow();
                ioService.printFormattedLine("No, right answer is: %s", rightAnswer.text());
            }
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
