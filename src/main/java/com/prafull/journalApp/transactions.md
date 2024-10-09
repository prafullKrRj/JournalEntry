In the context of your Spring Boot application, transactions are managed using the `@Transactional` annotation. Here are
the key details:

### What is a Transaction?

A transaction is a sequence of operations performed as a single logical unit of work. A transaction has four key
properties, often referred to as ACID properties:

- **Atomicity**: Ensures that all operations within the work unit are completed successfully; otherwise, the transaction
  is aborted at the point of failure, and all previous operations are rolled back to their former state.
- **Consistency**: Ensures that the database properly changes states upon a successfully committed transaction.
- **Isolation**: Enables transactions to operate independently of and transparent to each other.
- **Durability**: Ensures that the result or effect of a committed transaction persists in case of a system failure.

### Using `@Transactional` in Spring Boot

The `@Transactional` annotation is used to manage transactions in Spring. It ensures that the methods annotated with it
are executed within a transaction context. If any exception occurs, the transaction will be rolled back.

### Example in Your Code

In your `JournalEntryService` class, the `saveEntry` method is annotated with `@Transactional`:

```java
@Transactional
public void saveEntry(JournalEntry entry, String username) throws Exception {
  try {
    entry.setDate(LocalDateTime.now());
    User user = userService.getUserByUsername(username);
    JournalEntry savedEntry = repo.save(entry);
    user.getJournalEntries().add(savedEntry);
    userService.saveUser(user);
    repo.save(entry);
  } catch (Exception e) {
    e.printStackTrace();
    throw e;
  }
}
```

### Configuration

In your `JournalAppApplication` class, you have enabled transaction management with the `@EnableTransactionManagement`
annotation:

```java

@SpringBootApplication
@EnableTransactionManagement
public class JournalAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(JournalAppApplication.class, args);
    }

  @Bean
  public PlatformTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }
}
```

### Key Points

- **Propagation**: Defines how transactions relate to each other. For example, `REQUIRED` (default) means the method
  must run within a transaction, and if there is no existing transaction, a new one will be started.
- **Isolation**: Defines the isolation level for the transaction, which can affect performance and consistency.
- **Rollback**: By default, transactions will be rolled back on runtime exceptions but not on checked exceptions. This
  behavior can be customized.

### Summary

Transactions in Spring Boot are managed using the `@Transactional` annotation, which ensures that a method runs within a
transaction context. If an exception occurs, the transaction will be rolled back. Transaction management is enabled with
the `@EnableTransactionManagement` annotation, and the `PlatformTransactionManager` bean is used to configure the
transaction manager.