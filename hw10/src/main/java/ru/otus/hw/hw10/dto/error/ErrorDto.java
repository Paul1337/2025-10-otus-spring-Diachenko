package ru.otus.hw.hw10.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ErrorDto {
    private ErrorType type;

    private String message = null;

    private List<String> details = new ArrayList<>();

    public ErrorDto(ErrorType type) {
        this.type = type;
    }

    public ErrorDto(ErrorType type, String message) {
        this.type = type;
        this.message = message;
    }

    public ErrorDto(ErrorType type, List<String> details) {
        this.type = type;
        this.details = details;
    }
}
