package com.prafull.journalApp.entities;


import lombok.Data;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/***
 *      @Document annotation is used to specify the name of the collection in the MongoDB database.
 *      @Data annotation is used to generate getters and setters for the fields.
 *      @Id annotation is used to specify the primary key of the document.
 *      @Indexed annotation is used to create an index on the username field and make it unique.
 *      @NotNull annotation is used to make the password field mandatory.
 *      @DBRef annotation is used to create a reference to the JournalEntry class.
 *      The UserEntity class is used to represent the user entity in the MongoDB database.
 *      The UserEntity class has the following fields:
 *      id: The primary key of the document.
 *      username: The username of the user.
 *      password: The password of the user.
 */


@Document(collection = "users")
@Data
public class User {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NotNull
    private String username;
    @NotNull
    private String password;

    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>();
}
/*
@DBRef annotation is used to create a reference to another document
stored in a different collection in MongoDB.
This means that instead of embedding the referenced document directly within the current document,
a reference (similar to a foreign key in relational databases) is stored.
When the document is retrieved, the referenced document can be fetched separately,
allowing for normalization and reducing data redundancy*/