package com.fundoonotes.fundoo_notes.repository;

import com.fundoonotes.fundoo_notes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    // Get all normal notes
    List<Note> findByUserIdAndIsTrashedFalse(Long userId);

    // Get specific note
    Optional<Note> findByIdAndUserId(Long id, Long userId);

    // Get pinned notes
    List<Note> findByUserIdAndIsPinnedTrueAndIsTrashedFalse(Long userId);

    // Get archived notes
    List<Note> findByUserIdAndIsArchivedTrueAndIsTrashedFalse(Long userId);

    // Get trashed notes
    List<Note> findByUserIdAndIsTrashedTrue(Long userId);

    // getAllNotes ke liye
    List<Note> findByUserIdAndIsTrashedFalseAndIsArchivedFalse(
            Long userId);

    //@Param  --> [method variable ->jpql parameter] connect
    // Search by keyword in title or content
    // its a JPQL(Java Persistence Query Language)query...it uses enity name not table name..
    @Query("SELECT n FROM Note n WHERE n.user.id = :userId " +         // sirf us user ki notes jiski id match kre..
            "AND n.isTrashed = false " +                               // remove trashed notes..  n-->enity short name..
            "AND (LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")     // % is a wildcard..kuch bhi aa skta hai
    // @Param("userId") → JPQL query ke :userId parameter ko method ke userId variable se bind karta hai
    List<Note> searchNotes(@Param("userId") Long userId, @Param("keyword") String keyword);

    // Pending reminders fetch karenge..
    @Query("SELECT n FROM Note n WHERE " +
            "n.reminderTime IS NOT NULL AND " +
            "n.reminderTime <= :now AND " +
            "n.reminderSent = false AND " +
            "n.isTrashed = false")
    List<Note> findPendingReminders(@Param("now") LocalDateTime now);

    // Label se notes fetch krega
    @Query("SELECT n FROM Note n JOIN n.labels l WHERE l.id = :labelId AND n.user.id = :userId AND n.isTrashed = false")
    List<Note> findByLabelIdAndUserId(@Param("labelId") Long labelId, @Param("userId") Long userId);
}