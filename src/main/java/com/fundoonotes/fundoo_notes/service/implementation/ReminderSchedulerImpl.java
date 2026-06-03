package com.fundoonotes.fundoo_notes.service.implementation;

import com.fundoonotes.fundoo_notes.model.Note;
import com.fundoonotes.fundoo_notes.model.User;
import com.fundoonotes.fundoo_notes.repository.NoteRepository;
import com.fundoonotes.fundoo_notes.service.EmailProducer;
import com.fundoonotes.fundoo_notes.service.ReminderScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ReminderSchedulerImpl implements ReminderScheduler {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private EmailProducer emailProducer;

    @Override
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkReminders() {
        // Use IST timezone to match how frontend saves reminder time
        LocalDateTime nowIST = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        log.info("Checking reminders at IST: {}", nowIST);

        List<Note> pendingNotes = noteRepository.findPendingReminders(nowIST);
        log.info("Found {} pending reminders", pendingNotes.size());

        List<Long> noteIds = pendingNotes.stream()
                .map(Note::getId)
                .collect(Collectors.toList());

        for (Long noteId : noteIds) {
            noteRepository.findById(noteId).ifPresent(note -> {
                User user = note.getUser();
                emailProducer.sendEmailMessage(
                        user.getEmail(),
                        "Fundoo Notes - Reminder!",
                        "Hello " + user.getName() + "!\n\n" +
                                "Reminder for your note:\n" +
                                "Title: " + note.getTitle() + "\n" +
                                "Content: " + note.getContent() + "\n\n" +
                                "Regards,\nFundoo Notes Team"
                );
                note.setReminderSent(true);
                noteRepository.save(note);
                log.info("Reminder sent for note: {} to: {}",
                        note.getId(), user.getEmail());
            });
        }
    }
}
