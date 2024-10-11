package com.prafull.journalApp.controllers;

import com.prafull.journalApp.entities.JournalEntry;
import com.prafull.journalApp.entities.UserEntity;
import com.prafull.journalApp.service.JournalEntryService;
import com.prafull.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {


    @Autowired
    private JournalEntryService service;
    @Autowired
    private UserService userService;

    @DeleteMapping("/delete-entry/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable ObjectId id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (service.deleteById(id, auth.getName())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping("/create-entry")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            service.saveEntry(entry, auth.getName());
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/entry/{id}")
    public ResponseEntity<JournalEntry> getSingleEntry(@PathVariable ObjectId id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            JournalEntry entry = service.getSingle(id, auth.getName());
            if (entry != null) {
                return ResponseEntity.ok(entry);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/entries")
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserEntity userEntity = userService.getUserByUsername(auth.getName());
            List<JournalEntry> entries = userEntity.getJournalEntries();
            if (entries != null) {
                return ResponseEntity.ok(entries);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No entries found for user: " + auth.getName());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found with username: " + SecurityContextHolder.getContext().getAuthentication().getName());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEntry(@RequestBody JournalEntry entry, @PathVariable ObjectId id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            JournalEntry old = service.getSingle(id, auth.getName());
            System.out.println("Old entry: " + old);
            if (old != null) {
                old.setTitle(!entry.getTitle().isEmpty() ? entry.getTitle() : old.getTitle());
                old.setContent(!entry.getContent().isEmpty() ? entry.getContent() : old.getContent());
                service.saveEntry(old, auth.getName());
                return ResponseEntity.ok(old);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No entry found with id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the entry.");
        }
    }
}
