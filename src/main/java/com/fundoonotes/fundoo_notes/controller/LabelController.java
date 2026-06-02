package com.fundoonotes.fundoo_notes.controller;

import com.fundoonotes.fundoo_notes.dto.ApiResponseDTO;
import com.fundoonotes.fundoo_notes.dto.LabelRequestDTO;
import com.fundoonotes.fundoo_notes.dto.LabelResponseDTO;
import com.fundoonotes.fundoo_notes.service.LabelService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/labels")
@Slf4j
public class LabelController {

    @Autowired
    private LabelService labelService;

    // Token se userId nikaalane ka helper method
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }

    // POST /api/labels
    @PostMapping
    public ResponseEntity<ApiResponseDTO<LabelResponseDTO>> createLabel(@Valid @RequestBody LabelRequestDTO request) {

        Long userId = getCurrentUserId();
        log.info("Create label request for userId: {}", userId);

        ApiResponseDTO<LabelResponseDTO> response = labelService.createLabel(userId, request);

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // GET /api/labels
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LabelResponseDTO>>> getAllLabels() {

        Long userId = getCurrentUserId();
        log.info("Get all labels for userId: {}", userId);

        ApiResponseDTO<List<LabelResponseDTO>> response = labelService.getAllLabels(userId);

        return ResponseEntity.ok(response);
    }

    // DELETE /api/labels/{labelId}
    @DeleteMapping("/{labelId}")
    public ResponseEntity<ApiResponseDTO<String>> deleteLabel(@PathVariable Long labelId) {

        Long userId = getCurrentUserId();
        log.info("Delete label request for labelId: {}", labelId);

        ApiResponseDTO<String> response = labelService.deleteLabel(userId, labelId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}