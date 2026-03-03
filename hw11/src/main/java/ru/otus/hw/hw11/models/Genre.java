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
@Document(collection = "genres")
public class Genre {
    @Id
    private String id;

    @Setter
    @Field(name = "name")
    private String name;
}
