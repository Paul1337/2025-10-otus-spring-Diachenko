package ru.otus.hw.hw08.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "authors")
@Getter
public class Author {
    @Id
    private String id;

    @Field(name = "fullName")
    private String fullName;
}
