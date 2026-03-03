package ru.otus.hw.hw11.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    @Setter
    @Field(name = "text")
    private String text;

    @Setter
    private String bookId;
}
