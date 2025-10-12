package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

public class SimpleQuestionToStringConverter implements QuestionToStringConverter {
    @Override
    public String convert(Question question) {
        StringBuilder resultBuilder = new StringBuilder();

        resultBuilder.append(question.text() + "\n");
        for (int i = 0; i < question.answers().size(); i++) {
            var answer = question.answers().get(i);
            resultBuilder.append("%d) %s \n".formatted(i + 1, answer.text()));
        }

        return resultBuilder.toString();
    }
}
