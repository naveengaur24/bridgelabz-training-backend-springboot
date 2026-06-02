package com.fundoonotes.fundoo_notes.service;

import com.fundoonotes.fundoo_notes.dto.ApiResponseDTO;
import com.fundoonotes.fundoo_notes.dto.LabelRequestDTO;
import com.fundoonotes.fundoo_notes.dto.LabelResponseDTO;
import java.util.List;

public interface LabelService {

    ApiResponseDTO<LabelResponseDTO> createLabel(Long userId, LabelRequestDTO request);

    ApiResponseDTO<List<LabelResponseDTO>> getAllLabels(Long userId);

    ApiResponseDTO<String> deleteLabel(Long userId, Long labelId);
}