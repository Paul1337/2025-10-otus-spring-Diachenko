package ru.otus.hw.hw11.dto;

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
    private String id;

    @NotEmpty
    private String title;

    @NotNull
    private String authorId;

    @NotEmpty
    private Set<String> genreIds;

    public UpdateBookDto(String title, String authorId, Set<String> genreIds) {
        this.title = title;
        this.authorId = authorId;
        this.genreIds = genreIds;
    }
}
