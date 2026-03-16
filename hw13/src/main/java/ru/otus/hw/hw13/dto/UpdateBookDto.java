package ru.otus.hw.hw13.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookDto {
    private Long id;

    @NotEmpty
    private String title;

    private Long authorId;

    @NotEmpty
    private Set<Long> genreIds;

    public UpdateBookDto(String title, Long authorId, Set<Long> genreIds) {
        this.title = title;
        this.authorId = authorId;
        this.genreIds = genreIds;
    }
}
