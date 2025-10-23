package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    private InputStream getResourceAsStream(String fileName) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (stream == null) {
            throw new QuestionReadException("Required resource '%s' not found in classpath. ".formatted(fileName));
        }
        return stream;
    }

    @Override
    public List<Question> findAll() {
        String fileName = fileNameProvider.getTestFileName();
        try (InputStream testFileInputStream = getResourceAsStream(fileName);
             InputStreamReader inputStreamReader = new InputStreamReader(testFileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        ) {
            var csvToQuestionDtoBuilder = new CsvToBeanBuilder<QuestionDto>(bufferedReader);
            var questionDtos = csvToQuestionDtoBuilder.withType(QuestionDto.class)
                    .withSkipLines(1).withSeparator(';').build().parse();
            return questionDtos.stream().map(QuestionDto::toDomainObject).toList();
        } catch (IOException e) {
            throw new QuestionReadException("File with test questions not found", e);
        }
    }
}
