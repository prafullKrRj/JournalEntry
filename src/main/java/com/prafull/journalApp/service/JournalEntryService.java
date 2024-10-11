package com.prafull.journalApp.service;

import com.prafull.journalApp.entities.JournalEntry;
import com.prafull.journalApp.entities.UserEntity;
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
            UserEntity userEntity = userService.getUserByUsername(username);
            JournalEntry savedEntry = repo.save(entry);
            userEntity.getJournalEntries().stream().filter(e -> e.getId().equals(entry.getId())).findFirst().ifPresent(e -> {
                e.setTitle(savedEntry.getTitle());
                e.setContent(savedEntry.getContent());
                e.setDate(savedEntry.getDate());
            });
            userService.saveUser(userEntity);
            repo.save(entry);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<JournalEntry> getAll() {
        return repo.findAll();
    }

    public JournalEntry getSingle(ObjectId id, String username) {
        UserEntity userEntity = userService.getUserByUsername(username);
        Optional<JournalEntry> find = userEntity.getJournalEntries().stream().filter(entry -> entry.getId().equals(id)).findFirst();
        return find.orElse(null);
    }

    @Transactional
    public Boolean deleteById(ObjectId id, String username) {
        UserEntity userEntity = userService.getUserByUsername(username);
        boolean removed = userEntity.getJournalEntries().removeIf(entry -> entry.getId().equals(id));
        if (removed) {
            userService.saveUser(userEntity);
            repo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
