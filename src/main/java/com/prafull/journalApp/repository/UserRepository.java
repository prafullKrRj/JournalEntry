package com.prafull.journalApp.repository;

import com.prafull.journalApp.entities.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {

    UserEntity findByUsername(String username);


    void deleteByUsername(String username);
}
