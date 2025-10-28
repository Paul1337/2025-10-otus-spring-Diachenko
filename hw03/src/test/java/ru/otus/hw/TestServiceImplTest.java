package ru.otus.hw;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.QuestionToStringConverter;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestServiceImplTest {
    @Mock
    private LocalizedIOService ioService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    private QuestionToStringConverter questionToStringConverter;

    @InjectMocks
    private TestServiceImpl testService;

    private AutoCloseable openedMocks;

    private Student student;

    @BeforeEach
    void setUp() {
        openedMocks = MockitoAnnotations.openMocks(this);
        student = new Student("John", "Doe");
    }

    @AfterEach
    void tearDown() throws Exception {
        openedMocks.close();
    }

    @Test
    void executeTestWhen2QuestionsShouldPrintAllQuestions() {
        List<Question> questions = createTestQuestions();
        when(questionDao.findAll()).thenReturn(questions);
        when(questionToStringConverter.convert(any(Question.class)))
                .thenReturn("Question 1: What is Java?", "Question 2: What is Spring?");
        when(ioService.getMessage("TestService.answer.not.in.range")).thenReturn("Not in range!");

        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString())).thenReturn(1, 2);
        int assumingRightAnswersCount = 1;

        TestResult result = testService.executeTestFor(student);

        assertNotNull(result);
        assertEquals(student, result.getStudent());
        assertEquals(questions.size(), result.getAnsweredQuestions().size());
        assertEquals(assumingRightAnswersCount, result.getRightAnswersCount());

        verify(ioService).printLine("");
        verify(ioService).printFormattedLineLocalized("TestService.answer.the.questions");
        verify(ioService, times(assumingRightAnswersCount)).printLineLocalized("TestService.answer.right");

        verify(questionDao, times(1)).findAll();
        verify(questionToStringConverter, times(questions.size())).convert(any(Question.class));
        verify(ioService, times(questions.size())).readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString());
    }

    @Test
    void executeTestWhenNoQuestionsShouldPrintOnlyHeaders() {
        List<Question> questions = List.of();
        when(questionDao.findAll()).thenReturn(questions);

        TestResult result = testService.executeTestFor(student);

        assertNotNull(result);
        assertEquals(student, result.getStudent());
        assertEquals(0, result.getAnsweredQuestions().size());
        assertEquals(0, result.getRightAnswersCount());

        verify(ioService).printLine("");
        verify(ioService).printFormattedLineLocalized("TestService.answer.the.questions");
        verify(ioService, never()).printLineLocalized("TestService.answer.right");
        verify(ioService, never()).printLineLocalized("TestService.answer.wrong");

        verify(questionDao, times(1)).findAll();
        verify(questionToStringConverter, never()).convert(any(Question.class));
        verify(ioService, never()).readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString());
    }

    private List<Question> createTestQuestions() {
        Answer answer1 = new Answer("Programming language", true);
        Answer answer2 = new Answer("Coffee", false);
        Answer answer3 = new Answer("Car", false);

        Answer answer4 = new Answer("Framework", true);
        Answer answer5 = new Answer("Language", false);
        Answer answer6 = new Answer("Database", false);

        Question question1 = new Question("What is Java?", List.of(answer1, answer2, answer3));
        Question question2 = new Question("What is Spring?", List.of(answer4, answer5, answer6));

        return List.of(question1, question2);
    }

    private Question createSingleQuestion() {
        Answer answer1 = new Answer("Correct", true);
        Answer answer2 = new Answer("Wrong", false);
        return new Question("Single question?", List.of(answer1, answer2));
    }
}
