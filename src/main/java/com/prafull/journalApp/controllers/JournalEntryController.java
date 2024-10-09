package com.prafull.journalApp.controllers;

import com.prafull.journalApp.entities.JournalEntry;
import com.prafull.journalApp.entities.User;
import com.prafull.journalApp.service.JournalEntryService;
import com.prafull.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {


    @Autowired
    private JournalEntryService service;

    @Autowired
    private UserService userService;


    @DeleteMapping("/{username}/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable ObjectId id, @PathVariable String username) {
        service.deleteById(id, username);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry, @PathVariable String username) {
        try {
            service.saveEntry(entry, username);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/entry/{id}")
    public ResponseEntity<JournalEntry> getSingleEntry(@PathVariable ObjectId id) {
        JournalEntry entry = service.getSingle(id);
        return ResponseEntity.ok(entry);
    }

    @GetMapping("user/{username}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            System.out.println(username);
            List<JournalEntry> entries = user.getJournalEntries();
            if (entries != null) {
                return ResponseEntity.ok(entries);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No entries found for user: " + username);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found with username: " + username);
        }
    }

    @PostMapping("/{id}/{username}")
    public ResponseEntity<?> updateEntry(@RequestBody JournalEntry entry, @PathVariable ObjectId id, @PathVariable String username) {
        try {
            JournalEntry old = service.getSingle(id);
            if (old != null) {
                old.setTitle(!entry.getTitle().isEmpty() ? entry.getTitle() : old.getTitle());
                old.setContent(!entry.getContent().isEmpty() ? entry.getContent() : old.getContent());
                service.saveEntry(old, username);
            }
            return ResponseEntity.ok(old);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the entry.");
        }
    }
}
