package ru.otus.hw;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = { CsvQuestionDao.class })
public class CsvQuestionDaoTest {
    @MockitoBean
    private AppProperties testAppProperties;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @Test
    void findAllWhenTestFileNameCorrectShouldWork() {
        when(testAppProperties.getTestFileName()).thenReturn("questions.csv");
        var foundQuestions = csvQuestionDao.findAll();
        assertEquals(8, foundQuestions.size());
        for (var question: foundQuestions) {
            assertNotNull(question.text());
            assertNotNull(question.answers());
        }
    }

    @Test
    void findAllWhenTestFileNameNotCorrectShouldThrowException() {
        when(testAppProperties.getTestFileName()).thenReturn("not-correct-filename.csv");
        assertThrows(QuestionReadException.class, () -> csvQuestionDao.findAll());
    }
}
