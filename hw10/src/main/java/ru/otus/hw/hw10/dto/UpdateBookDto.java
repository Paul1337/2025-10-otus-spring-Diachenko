package ru.otus.hw.hw10.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookDto {
    private Long id;

    private String title;

    private Long authorId;

    private Set<Long> genreIds;

    public UpdateBookDto(String title, Long authorId, Set<Long> genreIds) {
        this.title = title;
        this.authorId = authorId;
        this.genreIds = genreIds;
    }
}
