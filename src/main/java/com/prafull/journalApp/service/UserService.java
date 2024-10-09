package com.prafull.journalApp.service;


import com.prafull.journalApp.entities.User;
import com.prafull.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUser(ObjectId userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void deleteUser(ObjectId userId) {
        userRepository.deleteById(userId);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
