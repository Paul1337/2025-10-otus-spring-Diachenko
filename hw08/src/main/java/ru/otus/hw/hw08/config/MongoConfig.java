package ru.otus.hw.hw08.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.hw.hw08.services.BookDeleteMongoListener;

@Configuration
public class MongoConfig {

    @Bean
    public BookDeleteMongoListener bookDeleteMongoListener(MongoOperations mongoOperations) {
        return new BookDeleteMongoListener(mongoOperations);
    }
}
