package com.fundoonotes.fundoo_notes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponseDTO {
    private Long id;
    private String title;
    private String content;
    private boolean isPinned;
    private boolean isArchived;
    private boolean isTrashed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime reminderTime;
    private boolean reminderSent;
    private Set<LabelResponseDTO> labels;
}
