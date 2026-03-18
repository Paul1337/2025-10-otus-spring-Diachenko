package ru.otus.hw.hw13.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDto {
    @NotEmpty
    private String title;

    private Long authorId;

    @NotEmpty
    private Set<Long> genreIds;

    public CreateBookDto(String title, Set<Long> genreIds) {
        this.title = title;
        this.genreIds = genreIds;
        this.authorId = null;
    }
}
