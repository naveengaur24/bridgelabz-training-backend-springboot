package com.fundoonotes.fundoo_notes.repository;

import com.fundoonotes.fundoo_notes.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

    // User ki saari labels fetch karega
    List<Label> findByUserId(Long userId);

    // Label naam se dhundho (duplicate check)
    Optional<Label> findByNameAndUserId(String name, Long userId);

    // Label exist karta hai?
    boolean existsByNameAndUserId(String name, Long userId);
}