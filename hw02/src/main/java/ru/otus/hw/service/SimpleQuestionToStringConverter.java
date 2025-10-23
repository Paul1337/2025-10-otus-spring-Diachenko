package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Question;

@Service
public class SimpleQuestionToStringConverter implements QuestionToStringConverter {
    @Override
    public String convert(Question question) {
        StringBuilder resultBuilder = new StringBuilder();

        resultBuilder.append(question.text() + "\n");
        for (int i = 0; i < question.answers().size(); i++) {
            var answer = question.answers().get(i);
            resultBuilder.append("%d) %s".formatted(i + 1, answer.text()));
            if (i + 1 < question.answers().size()) {
                resultBuilder.append("\n");
            }
        }

        return resultBuilder.toString();
    }
}
