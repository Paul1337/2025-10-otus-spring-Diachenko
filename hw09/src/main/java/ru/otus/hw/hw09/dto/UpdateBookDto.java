package ru.otus.hw.hw09.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookDto {
    private long id;

    private String title;

    private long authorId;

    private Set<Long> genreIds;
}
