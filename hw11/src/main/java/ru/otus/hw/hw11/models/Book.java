package ru.otus.hw.hw11.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    @Setter
    @Field(name = "title")
    private String title;

    @Setter
    private Author author;

    @Setter
    private Set<Genre> genres;
}
