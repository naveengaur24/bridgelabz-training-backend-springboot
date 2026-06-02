package com.fundoonotes.fundoo_notes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LabelRequestDTO {

    @NotBlank(message = "Label name cannot be empty!")
    private String name;
}