package com.prafull.journalApp.service;


import com.prafull.journalApp.entities.UserEntity;
import com.prafull.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public void saveNewUser(UserEntity userEntity) {
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(List.of("USER"));
        userRepository.save(userEntity);
    }

    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }

    public UserEntity getUser(ObjectId userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
