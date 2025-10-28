package ru.otus.hw;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CsvQuestionDaoTest {
    @Mock
    private TestFileNameProvider testFileNameProvider;

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

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
    void findAllWhenTestFileNameCorrectShouldWork() {
        when(testFileNameProvider.getTestFileName()).thenReturn("questions.csv");
        var foundQuestions = csvQuestionDao.findAll();
        assertEquals(8, foundQuestions.size());
        for (var question: foundQuestions) {
            assertNotNull(question.text());
            assertNotNull(question.answers());
        }
    }

    @Test
    void findAllWhenTestFileNameNotCorrectShouldThrowException() {
        when(testFileNameProvider.getTestFileName()).thenReturn("not-correct-filename.csv");
        assertThrows(QuestionReadException.class, () -> csvQuestionDao.findAll());
    }
}
