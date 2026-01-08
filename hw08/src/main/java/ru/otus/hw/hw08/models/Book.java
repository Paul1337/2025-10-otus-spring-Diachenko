package ru.otus.hw.hw08.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
@Getter
public class Book {
    @Id
    private String id;

    @Field(name = "title")
    @Setter
    private String title;

    @Field(name = "authorId")
    @Setter
    private String authorId;

    @Field(name = "genreIds")
    @Setter
    private List<String> genreIds = new ArrayList<>();
}
