package ru.otus.hw.hw08.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comments")
@Getter
public class Comment {
    @Id
    private String id;

    @Field(name = "text")
    @Setter
    private String text;

    @DBRef(lazy = true)
    private Book book;
}
