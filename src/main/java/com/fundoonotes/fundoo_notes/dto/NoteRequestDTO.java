package com.fundoonotes.fundoo_notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;


@Data
public class NoteRequestDTO {

    @NotBlank(message = "Title cannot be empty!")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters!")
    private String title;

    @Size(max = 5000, message = "Content cannot exceed 5000 characters!")
    private String content;

    private LocalDateTime reminderTime;   // it's a date-time object check current time..
}