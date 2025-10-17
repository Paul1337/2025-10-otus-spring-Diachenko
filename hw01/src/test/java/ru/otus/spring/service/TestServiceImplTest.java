package ru.otus.spring.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.QuestionToStringConverter;
import ru.otus.hw.service.TestServiceImpl;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.never;

public class TestServiceImplTest {
    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    private QuestionToStringConverter questionToStringConverter;

    @InjectMocks
    private TestServiceImpl testService;

    private AutoCloseable openedMocks;

    @BeforeEach
    void setUp() {
        openedMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openedMocks.close();
    }

    @Test
    void executeTestWhen3QuestionsShouldPrintAllQuestions() {
        List<Question> questions = List.of(new Question("text", List.of()), new Question("text", List.of()), new Question("text", List.of()));

        when(questionDao.findAll()).thenReturn(questions);
        when(questionToStringConverter.convert(any(Question.class)))
                .thenReturn("Formatted Question 1")
                .thenReturn("Formatted Question 2")
                .thenReturn("Formatted Question 3");

        testService.executeTest();

        verify(questionDao, times(1)).findAll();
        verify(questionToStringConverter, times(questions.size())).convert(any(Question.class));

        verify(ioService).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");
        verify(ioService).printLine("Formatted Question 1");
        verify(ioService).printLine("Formatted Question 2");
        verify(ioService).printLine("Formatted Question 3");

        verifyNoMoreInteractions(ioService, questionDao, questionToStringConverter);

        System.out.println("123");
        System.out.println("123");
        System.out.println("123");
        System.out.println("123");
        System.out.println("123");
        System.out.println("123");
        System.out.println("123");
        System.out.println("123");
        System.out.println("123");
    }

    @Test
    void executeTestWhenNoQuestionsShouldPrintOnlyHeaders() {
        List<Question> questions = List.of();

        when(questionDao.findAll()).thenReturn(questions);

        testService.executeTest();

        verify(questionDao, times(1)).findAll();
        verify(questionToStringConverter, never()).convert(any(Question.class));

        verify(ioService).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");

        verifyNoMoreInteractions(ioService, questionDao, questionToStringConverter);
    }
}
