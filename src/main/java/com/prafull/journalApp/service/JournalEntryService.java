package com.prafull.journalApp.service;

import com.prafull.journalApp.entities.JournalEntry;
import com.prafull.journalApp.entities.User;
import com.prafull.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// controller -> service -> repository

/**
 * @Transactional annotation is used to manage transactions in Spring.
 * It ensures that the methods annotated with it are executed within a transaction context.
 * If any exception occurs, the transaction will be rolled back.
 */
@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo repo;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry entry, String username) throws Exception {
        try {
            entry.setDate(LocalDateTime.now());
            User user = userService.getUserByUsername(username);
            JournalEntry savedEntry = repo.save(entry);
            user.getJournalEntries().add(savedEntry);
            userService.saveUser(user);
            repo.save(entry);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<JournalEntry> getAll() {
        return repo.findAll();
    }

    public JournalEntry getSingle(ObjectId id) {
        Optional<JournalEntry> find = repo.findById(id);
        return find.orElse(null);
    }

    public void deleteById(ObjectId id, String username) {
        User user = userService.getUserByUsername(username);
        user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));
        userService.saveUser(user);
        repo.deleteById(id);
    }
}
