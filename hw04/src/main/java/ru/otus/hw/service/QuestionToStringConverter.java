package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

public interface QuestionToStringConverter {
    String convert(Question question);
}
