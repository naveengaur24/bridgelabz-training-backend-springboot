package com.fundoonotes.fundoo_notes.service.implementation;

import com.fundoonotes.fundoo_notes.dto.ApiResponseDTO;
import com.fundoonotes.fundoo_notes.dto.LabelRequestDTO;
import com.fundoonotes.fundoo_notes.dto.LabelResponseDTO;
import com.fundoonotes.fundoo_notes.exception.UserNotFoundException;
import com.fundoonotes.fundoo_notes.model.Label;
import com.fundoonotes.fundoo_notes.model.User;
import com.fundoonotes.fundoo_notes.repository.LabelRepository;
import com.fundoonotes.fundoo_notes.repository.UserRepository;
import com.fundoonotes.fundoo_notes.service.LabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ApiResponseDTO<LabelResponseDTO> createLabel(
            Long userId, LabelRequestDTO request) {
        log.info("Creating label for userId: {}", userId);

        // User exist karta hai?
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found!");
        }

        // Duplicate label check kia
        if (labelRepository.existsByNameAndUserId(
                request.getName(), userId)) {
            return new ApiResponseDTO<>(false,
                    "Label already exists!", null);
        }

        // Label banao
        Label label = new Label();
        label.setName(request.getName());
        label.setUser(userOptional.get());

        Label savedLabel = labelRepository.save(label);
        log.info("Label created with id: {}", savedLabel.getId());

        return new ApiResponseDTO<>(true,
                "Label created successfully!",
                convertToResponseDTO(savedLabel));
    }

    @Override
    public ApiResponseDTO<List<LabelResponseDTO>> getAllLabels(Long userId) {
        log.info("Fetching all labels for userId: {}", userId);

        List<Label> labels = labelRepository.findByUserId(userId);

        List<LabelResponseDTO> responseDTOs = labels.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return new ApiResponseDTO<>(true,
                "Labels fetched successfully!",
                responseDTOs);
    }

    @Override
    public ApiResponseDTO<String> deleteLabel(Long userId, Long labelId) {
        log.info("Deleting label id: {} for userId: {}", labelId, userId);

        // Label dhundho
        Optional<Label> labelOptional = labelRepository.findById(labelId);
        if (labelOptional.isEmpty()) {
            return new ApiResponseDTO<>(false, "Label not found!", null);
        }

        // Label user ki hai?
        Label label = labelOptional.get();
        if (!label.getUser().getId().equals(userId)) {
            return new ApiResponseDTO<>(false,
                    "Unauthorized!", null);
        }

        labelRepository.delete(label);
        log.info("Label deleted: {}", labelId);

        return new ApiResponseDTO<>(true,
                "Label deleted successfully!", null);
    }

    private LabelResponseDTO convertToResponseDTO(Label label) {
        return new LabelResponseDTO(
                label.getId(),
                label.getName(),
                label.getCreatedAt()
        );
    }
}