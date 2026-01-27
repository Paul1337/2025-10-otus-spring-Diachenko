package ru.otus.hw.hw09.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookDto {
    private Long id;

    private String title;

    private Long authorId;

    private Set<Long> genreIds;
}
