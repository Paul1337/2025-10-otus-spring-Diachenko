package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionToStringConverter questionToStringConverter;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLineLocalized("TestService.answer.the.questions");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var questionAsString = questionToStringConverter.convert(question);
            var prompt = ioService.getMessage("TestService.answer.not.in.range");
            var answerIndex = ioService.readIntForRangeWithPrompt(
                    1, question.answers().size(), questionAsString, prompt
            ) - 1;
            var isAnswerValid = question.answers().get(answerIndex).isCorrect();
            if (isAnswerValid) {
                ioService.printLineLocalized("TestService.answer.right");
            } else {
                var rightAnswer = question.answers().stream().filter(Answer::isCorrect).findAny().orElseThrow();
                ioService.printFormattedLineLocalized("TestService.answer.wrong", rightAnswer.text());
            }
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

}
