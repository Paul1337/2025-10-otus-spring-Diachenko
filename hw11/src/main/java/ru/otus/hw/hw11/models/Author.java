package ru.otus.hw.hw11.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document(collection = "authors")
public class Author {
    @Id
    private String id;

    @Setter
    @Field(name = "fullName")
    private String fullName;
}
