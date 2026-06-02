package com.fundoonotes.fundoo_notes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelResponseDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}