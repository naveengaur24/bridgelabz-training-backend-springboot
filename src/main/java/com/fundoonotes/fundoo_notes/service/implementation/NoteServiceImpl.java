package com.fundoonotes.fundoo_notes.service.implementation;

import com.fundoonotes.fundoo_notes.dto.ApiResponseDTO;
import com.fundoonotes.fundoo_notes.dto.LabelResponseDTO;
import com.fundoonotes.fundoo_notes.dto.NoteRequestDTO;
import com.fundoonotes.fundoo_notes.dto.NoteResponseDTO;
import com.fundoonotes.fundoo_notes.exception.NoteNotFoundException;
import com.fundoonotes.fundoo_notes.exception.UserNotFoundException;
import com.fundoonotes.fundoo_notes.model.Label;
import com.fundoonotes.fundoo_notes.model.Note;
import com.fundoonotes.fundoo_notes.model.User;
import com.fundoonotes.fundoo_notes.repository.LabelRepository;
import com.fundoonotes.fundoo_notes.repository.NoteRepository;
import com.fundoonotes.fundoo_notes.repository.UserRepository;
import com.fundoonotes.fundoo_notes.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional  // ← Class level pe lagao — sab methods pe apply hoga
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public ApiResponseDTO<NoteResponseDTO> createNote(Long userId, NoteRequestDTO request) throws UserNotFoundException {
        log.info("Creating note for userId: {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found!");
        }

        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setUser(userOptional.get());

        // --> here we set reminder time if user gave it is optional
        if (request.getReminderTime() != null) {
            note.setReminderTime(request.getReminderTime());
            log.info("Reminder set for: {}", request.getReminderTime());
        }
        Note savedNote = noteRepository.save(note);
        log.info("Note created with id: {}", savedNote.getId());

        return new ApiResponseDTO<>(true, "Note created successfully!", convertToResponseDTO(savedNote));
    }

    @Override
    public ApiResponseDTO<List<NoteResponseDTO>> getAllNotes(Long userId) {
        log.info("Fetching all notes for userId: {}", userId);

        // List<Note> notes = noteRepository.findByUserIdAndIsTrashedFalse(userId);

        List<Note> notes = noteRepository
                .findByUserIdAndIsTrashedFalseAndIsArchivedFalse(userId);

        List<NoteResponseDTO> responseDTOs = notes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return new ApiResponseDTO<>(
                true, "Notes fetched successfully!", responseDTOs);
    }

    @Override
    public ApiResponseDTO<NoteResponseDTO> getNoteById(Long userId, Long noteId) {
        log.info("Fetching note id: {} for userId: {}", noteId, userId);

        Optional<Note> noteOptional = noteRepository.findByIdAndUserId(noteId, userId);
        if (noteOptional.isEmpty()) {
            throw new NoteNotFoundException("Note not found!");
        }

        return new ApiResponseDTO<>(true, "Note fetched successfully!", convertToResponseDTO(noteOptional.get()));
    }

    @Override
    public ApiResponseDTO<NoteResponseDTO> updateNote(Long userId, Long noteId, NoteRequestDTO request) {
        log.info("Updating note id: {} for userId: {}", noteId, userId);

        Optional<Note> noteOptional = noteRepository.findByIdAndUserId(noteId, userId);
        if (noteOptional.isEmpty()) {
            throw new NoteNotFoundException("Note not found!");
        }
        Note note = noteOptional.get();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        // ReminderTime update
        if (request.getReminderTime() != null) {
            note.setReminderTime(request.getReminderTime());
            note.setReminderSent(false); // Reset karo — naya reminder set hua!
            log.info("Reminder updated for note: {}", noteId);
        }

        Note updatedNote = noteRepository.save(note);
        log.info("Note updated: {}", updatedNote.getId());

        return new ApiResponseDTO<>(true, "Note updated successfully!", convertToResponseDTO(updatedNote));
    }

    @Override
    public ApiResponseDTO<NoteResponseDTO> pinNote(Long userId, Long noteId) {
        log.info("Pinning note id: {} for userId: {}", noteId, userId);

        Optional<Note> noteOptional = noteRepository.findByIdAndUserId(noteId, userId);
        if (noteOptional.isEmpty()) {
            throw new NoteNotFoundException("Note not found!");
        }
        Note note = noteOptional.get();
        note.setPinned(!note.isPinned());

        Note savedNote = noteRepository.save(note);
        String message = savedNote.isPinned() ? "Note pinned!" : "Note unpinned!";
        return new ApiResponseDTO<>(true, message, convertToResponseDTO(savedNote));
    }

    @Override
    public ApiResponseDTO<NoteResponseDTO> archiveNote(Long userId, Long noteId) {
        log.info("Archiving note id: {} for userId: {}", noteId, userId);

        Optional<Note> noteOptional = noteRepository.findByIdAndUserId(noteId, userId);
        if (noteOptional.isEmpty()) {
            throw new NoteNotFoundException("Note not found!");
        }

        Note note = noteOptional.get();
        note.setArchived(!note.isArchived());

        Note savedNote = noteRepository.save(note);
        String message = savedNote.isArchived() ? "Note archived!" : "Note unarchived!";

        return new ApiResponseDTO<>(true, message, convertToResponseDTO(savedNote));
    }

    @Override
    public ApiResponseDTO<NoteResponseDTO> trashNote(Long userId, Long noteId) {
        log.info("Trashing note id: {} for userId: {}", noteId, userId);

        Optional<Note> noteOptional = noteRepository.findByIdAndUserId(noteId, userId);
        if (noteOptional.isEmpty()) {
            throw new NoteNotFoundException("Note not found!");
        }

        Note note = noteOptional.get();
        note.setTrashed(!note.isTrashed());

        Note savedNote = noteRepository.save(note);
        String message = savedNote.isTrashed() ? "Note trashed!" : "Note restored!";

        return new ApiResponseDTO<>(true, message, convertToResponseDTO(savedNote));
    }

    // we delete note after trashing ---first we must have to trash the note..
    @Override
    public ApiResponseDTO<String> deleteNotePermanently(Long userId, Long noteId) {
        log.info("Permanently deleting note id: {} for userId: {}", noteId, userId);

        Optional<Note> noteOptional = noteRepository.findByIdAndUserId(noteId, userId);
        if (noteOptional.isEmpty()) {
            throw new NoteNotFoundException("Note not found!");
        }

        Note note = noteOptional.get();

        // Sirf trashed notes permanently delete ho sakti hain
        if (!note.isTrashed()) {
            return new ApiResponseDTO<>(false, "Note must be trashed before permanent delete!", null);
        }

        noteRepository.delete(note);
        log.info("Note permanently deleted: {}", noteId);

        return new ApiResponseDTO<>(true, "Note permanently deleted!", null);
    }

    // search notes..using keyword
    @Override
    public ApiResponseDTO<List<NoteResponseDTO>> searchNotes(Long userId, String keyword) {
        log.info("Searching notes for userId: {} keyword: {}", userId, keyword);
        List<Note> notes = noteRepository.searchNotes(userId, keyword);
        List<NoteResponseDTO> responseDTOs = notes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return new ApiResponseDTO<>(true, "Search results for: " + keyword, responseDTOs);
    }

    // filtering notes..
    @Override
    public ApiResponseDTO<List<NoteResponseDTO>> getPinnedNotes(Long userId) {
        log.info("Fetching pinned notes for userId: {}", userId);
        List<Note> notes = noteRepository
                .findByUserIdAndIsPinnedTrueAndIsTrashedFalse(userId);
        List<NoteResponseDTO> responseDTOs = notes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return new ApiResponseDTO<>(true, "Pinned notes fetched!", responseDTOs);
    }

    @Override
    public ApiResponseDTO<List<NoteResponseDTO>> getArchivedNotes(Long userId) {
        log.info("Fetching archived notes for userId: {}", userId);
        List<Note> notes = noteRepository
                .findByUserIdAndIsArchivedTrueAndIsTrashedFalse(userId);
        List<NoteResponseDTO> responseDTOs = notes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return new ApiResponseDTO<>(true, "Archived notes fetched!", responseDTOs);
    }

    @Override
    public ApiResponseDTO<List<NoteResponseDTO>> getTrashedNotes(Long userId) {
        log.info("Fetching trashed notes for userId: {}", userId);
        List<Note> notes = noteRepository.findByUserIdAndIsTrashedTrue(userId);
        List<NoteResponseDTO> responseDTOs = notes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return new ApiResponseDTO<>(true, "Trashed notes fetched!", responseDTOs);
    }

    // add label to note
    @Override
    public ApiResponseDTO<NoteResponseDTO> addLabelToNote(
            Long userId, Long noteId, Long labelId) {
        log.info("Adding label {} to note {} for userId: {}",
                labelId, noteId, userId);

        Optional<Note> noteOptional =
                noteRepository.findByIdAndUserId(noteId, userId);
        if (noteOptional.isEmpty()) {
            throw new NoteNotFoundException("Note not found!");
        }

        Optional<Label> labelOptional = labelRepository.findById(labelId);
        if (labelOptional.isEmpty()) {
            return new ApiResponseDTO<>(false, "Label not found!", null);
        }

        Label label = labelOptional.get();
        if (!label.getUser().getId().equals(userId)) {
            return new ApiResponseDTO<>(false, "Unauthorized!", null);
        }

        Note note = noteOptional.get();

        // New HashSet mein add karo — ConcurrentModification avoid
        Set<Label> currentLabels = new HashSet<>(note.getLabels());
        currentLabels.add(label);
        note.setLabels(currentLabels);

        Note savedNote = noteRepository.save(note);
        log.info("Label added to note successfully!");

        return new ApiResponseDTO<>(true,
                "Label added to note!",
                convertToResponseDTO(savedNote));
    }

    // remove label from note
    @Override
    public ApiResponseDTO<NoteResponseDTO> removeLabelFromNote(
            Long userId, Long noteId, Long labelId) {
        log.info("Removing label {} from note {} for userId: {}",
                labelId, noteId, userId);

        Optional<Note> noteOptional =
                noteRepository.findByIdAndUserId(noteId, userId);
        if (noteOptional.isEmpty()) {
            throw new NoteNotFoundException("Note not found!");
        }

        Optional<Label> labelOptional = labelRepository.findById(labelId);
        if (labelOptional.isEmpty()) {
            return new ApiResponseDTO<>(false, "Label not found!", null);
        }

        Note note = noteOptional.get();

        // New HashSet mein remove karo
        Set<Label> currentLabels = new HashSet<>(note.getLabels());
        currentLabels.remove(labelOptional.get());
        note.setLabels(currentLabels);

        Note savedNote = noteRepository.save(note);
        log.info("Label removed from note successfully!");

        return new ApiResponseDTO<>(true,
                "Label removed from note!",
                convertToResponseDTO(savedNote));
    }

    // get notes by label
    @Override
    public ApiResponseDTO<List<NoteResponseDTO>> getNotesByLabel(
            Long userId, Long labelId) {
        log.info("Fetching notes by label {} for userId: {}",
                labelId, userId);

        List<Note> notes = noteRepository
                .findByLabelIdAndUserId(labelId, userId);

        List<NoteResponseDTO> responseDTOs = notes.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return new ApiResponseDTO<>(true,
                "Notes fetched by label!", responseDTOs);
    }

    private NoteResponseDTO convertToResponseDTO(Note note) {

        // Labels convert karo safely
        Set<LabelResponseDTO> labelDTOs = new HashSet<>();
        if (note.getLabels() != null) {
            labelDTOs = note.getLabels()
                    .stream()
                    .map(label -> new LabelResponseDTO(
                            label.getId(),
                            label.getName(),
                            label.getCreatedAt()
                    ))
                    .collect(Collectors.toSet());
        }

        return new NoteResponseDTO(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.isPinned(),
                note.isArchived(),
                note.isTrashed(),
                note.getCreatedAt(),
                note.getUpdatedAt(),
                note.getReminderTime(),
                note.isReminderSent(),
                labelDTOs
        );
    }
}