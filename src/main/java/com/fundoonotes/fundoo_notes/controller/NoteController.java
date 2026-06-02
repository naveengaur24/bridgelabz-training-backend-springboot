package com.fundoonotes.fundoo_notes.controller;

import com.fundoonotes.fundoo_notes.dto.ApiResponseDTO;
import com.fundoonotes.fundoo_notes.dto.NoteRequestDTO;
import com.fundoonotes.fundoo_notes.dto.NoteResponseDTO;
import com.fundoonotes.fundoo_notes.exception.UserNotFoundException;
import com.fundoonotes.fundoo_notes.service.NoteService;
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
@RequestMapping("/api/notes")
@Slf4j
public class NoteController {

    @Autowired
    private NoteService noteService;

    // Token se logged in userId nikaalane ka helper method
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }

    //Authentication is the object of spring security
    //SecurityContextHolder.getContext() --> Ye current request ka security data laata hai.
    //getAuthentication() --> Ye currently logged-in user ka authentication object return karta hai.

    // POST /api/notes
    @PostMapping
    public ResponseEntity<ApiResponseDTO<NoteResponseDTO>> createNote(@Valid @RequestBody NoteRequestDTO request) {

        Long userId = getCurrentUserId();
        log.info("Create note request for userId: {}", userId);

        ApiResponseDTO<NoteResponseDTO> response = noteService.createNote(userId, request);

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // GET /api/notes
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<NoteResponseDTO>>> getAllNotes() {

        Long userId = getCurrentUserId();
        log.info("Get all notes for userId: {}", userId);

        ApiResponseDTO<List<NoteResponseDTO>> response = noteService.getAllNotes(userId);
        return ResponseEntity.ok(response);
    }

    // GET /api/notes/5
    @GetMapping("/{noteId}")
    public ResponseEntity<ApiResponseDTO<NoteResponseDTO>> getNoteById(@PathVariable Long noteId) {

        Long userId = getCurrentUserId();

        ApiResponseDTO<NoteResponseDTO> response = noteService.getNoteById(userId, noteId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // PUT /api/notes/5
    @PutMapping("/{noteId}")
    public ResponseEntity<ApiResponseDTO<NoteResponseDTO>> updateNote(
            @PathVariable Long noteId,
            @Valid @RequestBody NoteRequestDTO request) {

        Long userId = getCurrentUserId();

        ApiResponseDTO<NoteResponseDTO> response = noteService.updateNote(userId, noteId, request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // PUT /api/notes/5/pin
    @PutMapping("/{noteId}/pin")
    public ResponseEntity<ApiResponseDTO<NoteResponseDTO>> pinNote(@PathVariable Long noteId) {

        Long userId = getCurrentUserId();

        ApiResponseDTO<NoteResponseDTO> response = noteService.pinNote(userId, noteId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // PUT /api/notes/5/archive
    @PutMapping("/{noteId}/archive")
    public ResponseEntity<ApiResponseDTO<NoteResponseDTO>> archiveNote(@PathVariable Long noteId) {

        Long userId = getCurrentUserId();

        ApiResponseDTO<NoteResponseDTO> response = noteService.archiveNote(userId, noteId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // PUT /api/notes/5/trash
    @PutMapping("/{noteId}/trash")
    public ResponseEntity<ApiResponseDTO<NoteResponseDTO>> trashNote(@PathVariable Long noteId) {

        Long userId = getCurrentUserId();

        ApiResponseDTO<NoteResponseDTO> response = noteService.trashNote(userId, noteId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // DELETE /api/notes/5
    @DeleteMapping("/{noteId}")
    public ResponseEntity<ApiResponseDTO<String>> deleteNotePermanently(@PathVariable Long noteId) {

        Long userId = getCurrentUserId();
        log.info("Delete note request for noteId: {} userId: {}", noteId, userId);

        ApiResponseDTO<String> response = noteService.deleteNotePermanently(userId, noteId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // GET /api/notes/search?keyword=meeting
    //@RequestParam -->  for searching after ? (/search?keyword=meeting)
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<NoteResponseDTO>>> searchNotes(@RequestParam String keyword) {

        Long userId = getCurrentUserId();
        log.info("Search notes for userId: {} keyword: {}", userId, keyword);

        ApiResponseDTO<List<NoteResponseDTO>> response = noteService.searchNotes(userId, keyword);
        return ResponseEntity.ok(response);
    }

    // GET /api/notes/pinned
    @GetMapping("/pinned")
    public ResponseEntity<ApiResponseDTO<List<NoteResponseDTO>>> getPinnedNotes() {

        Long userId = getCurrentUserId();

        ApiResponseDTO<List<NoteResponseDTO>> response =
                noteService.getPinnedNotes(userId);
        return ResponseEntity.ok(response);
    }

    // GET /api/notes/archived
    @GetMapping("/archived")
    public ResponseEntity<ApiResponseDTO<List<NoteResponseDTO>>> getArchivedNotes() {

        Long userId = getCurrentUserId();

        ApiResponseDTO<List<NoteResponseDTO>> response =
                noteService.getArchivedNotes(userId);
        return ResponseEntity.ok(response);
    }

    // GET /api/notes/trashed
    @GetMapping("/trashed")
    public ResponseEntity<ApiResponseDTO<List<NoteResponseDTO>>> getTrashedNotes() {

        Long userId = getCurrentUserId();

        ApiResponseDTO<List<NoteResponseDTO>> response =
                noteService.getTrashedNotes(userId);
        return ResponseEntity.ok(response);
    }
    // POST /api/notes/{noteId}/labels/{labelId}
    @PostMapping("/{noteId}/labels/{labelId}")
    public ResponseEntity<ApiResponseDTO<NoteResponseDTO>> addLabelToNote(
            @PathVariable Long noteId,
            @PathVariable Long labelId) {

        Long userId = getCurrentUserId();

        ApiResponseDTO<NoteResponseDTO> response =
                noteService.addLabelToNote(userId, noteId, labelId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // DELETE /api/notes/{noteId}/labels/{labelId}
    @DeleteMapping("/{noteId}/labels/{labelId}")
    public ResponseEntity<ApiResponseDTO<NoteResponseDTO>> removeLabelFromNote(
            @PathVariable Long noteId,
            @PathVariable Long labelId) {

        Long userId = getCurrentUserId();

        ApiResponseDTO<NoteResponseDTO> response =
                noteService.removeLabelFromNote(userId, noteId, labelId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // GET /api/notes/label/{labelId}
    @GetMapping("/label/{labelId}")
    public ResponseEntity<ApiResponseDTO<List<NoteResponseDTO>>> getNotesByLabel(
            @PathVariable Long labelId) {

        Long userId = getCurrentUserId();

        ApiResponseDTO<List<NoteResponseDTO>> response =
                noteService.getNotesByLabel(userId, labelId);

        return ResponseEntity.ok(response);
    }
}