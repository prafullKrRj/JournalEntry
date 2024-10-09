package com.prafull.journalApp.repository;

import com.prafull.journalApp.entities.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface JournalEntryRepo extends MongoRepository<JournalEntry, ObjectId> {

}
