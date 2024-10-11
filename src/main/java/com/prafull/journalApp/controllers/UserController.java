package com.prafull.journalApp.controllers;


import com.prafull.journalApp.entities.UserEntity;
import com.prafull.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userService.deleteUser(authentication.getName());
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to delete user");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserEntity userEntity) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity userEntity1 = userService.getUserByUsername(username);
            userEntity1.setPassword(userEntity.getPassword());
            userEntity1.setUsername(userEntity.getUsername());
            userEntity1.setJournalEntries(userEntity.getJournalEntries());
            userService.saveNewUser(userEntity1);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to update user");
        }
    }
}
