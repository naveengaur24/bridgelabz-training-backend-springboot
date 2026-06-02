package com.fundoonotes.fundoo_notes.service;

import com.fundoonotes.fundoo_notes.dto.ApiResponseDTO;
import com.fundoonotes.fundoo_notes.dto.NoteRequestDTO;
import com.fundoonotes.fundoo_notes.dto.NoteResponseDTO;
import com.fundoonotes.fundoo_notes.exception.UserNotFoundException;

import java.util.List;

public interface NoteService {

    ApiResponseDTO<NoteResponseDTO> createNote(Long userId, NoteRequestDTO request) throws UserNotFoundException;

    ApiResponseDTO<List<NoteResponseDTO>> getAllNotes(Long userId);

    ApiResponseDTO<NoteResponseDTO> getNoteById(Long userId, Long noteId);

    ApiResponseDTO<NoteResponseDTO> updateNote(Long userId, Long noteId, NoteRequestDTO request);

    ApiResponseDTO<NoteResponseDTO> pinNote(Long userId, Long noteId);

    ApiResponseDTO<NoteResponseDTO> archiveNote(Long userId, Long noteId);

    ApiResponseDTO<NoteResponseDTO> trashNote(Long userId, Long noteId);

    ApiResponseDTO<String> deleteNotePermanently(Long userId, Long noteId);

    // Search & Filter
    ApiResponseDTO<List<NoteResponseDTO>> searchNotes(Long userId, String keyword);

    ApiResponseDTO<List<NoteResponseDTO>> getPinnedNotes(Long userId);

    ApiResponseDTO<List<NoteResponseDTO>> getArchivedNotes(Long userId);

    ApiResponseDTO<List<NoteResponseDTO>> getTrashedNotes(Long userId);

    ApiResponseDTO<NoteResponseDTO> addLabelToNote(Long userId, Long noteId, Long labelId);

    ApiResponseDTO<NoteResponseDTO> removeLabelFromNote(Long userId, Long noteId, Long labelId);

    ApiResponseDTO<List<NoteResponseDTO>> getNotesByLabel(Long userId, Long labelId);
}