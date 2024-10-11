package com.prafull.journalApp.controllers;

import com.prafull.journalApp.entities.UserEntity;
import com.prafull.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;


    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody UserEntity userEntity) {
        try {
            userService.saveNewUser(userEntity);
            return ResponseEntity.ok("User created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to create user");
        }
    }

    @GetMapping("health-check")
    public String healthCheck() {
        return "Ok";
    }
}
