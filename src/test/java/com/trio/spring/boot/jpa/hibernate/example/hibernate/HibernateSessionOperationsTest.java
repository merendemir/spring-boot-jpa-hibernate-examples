package com.trio.spring.boot.jpa.hibernate.example.hibernate;

import com.trio.spring.boot.jpa.hibernate.example.repository.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// @Sql annotation to execute SQL scripts before and after the test class.
// We are pointing to SQL files in src/test/resources.
@Sql(
        scripts = "classpath:sql/init_users.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Sql(
        scripts = "classpath:sql/cleanup_users.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS
)
@DataJpaTest // Loads JPA components and configures a test database
@Slf4j
@DisplayName("Hibernate Session Operations Tests")
public class HibernateSessionOperationsTest {

    // This class will contain tests for basic Hibernate Session operations
    // such as retrieving, saving, updating, and deleting entities using the Session API.

    private final Long USER_ID_FOR_RETRIEVE = 1000L;
    private final Long USER_ID_FOR_DELETE = 1001L;
    private final Long USER_ID_FOR_UPDATE = 1002L;
    private final String RETRIEVE_USER_FIRST_NAME = "Alice";
    private final String RETRIEVE_USER_LAST_NAME = "Smith";

    private Session session;

    @BeforeEach
    void setUp() {
        SessionBuilder sessionBuilder = sessionFactory.withOptions();
        session = sessionBuilder.openSession();
    }

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @DisplayName("Should find an existing user using Session.get()")
    void shouldFindExistingUser() {
        log.info("::: shouldFindExistingUser ::: Calling session.get()");
        Users foundUser = session.get(Users.class, USER_ID_FOR_RETRIEVE); // Use session.get() to retrieve the user by ID

        assertNotNull(foundUser);
        assertEquals(USER_ID_FOR_RETRIEVE, foundUser.getId());
        assertEquals(RETRIEVE_USER_FIRST_NAME, foundUser.getFirstName());
        assertEquals(RETRIEVE_USER_LAST_NAME, foundUser.getLastName());
    }

    @Test
    @DisplayName("Should save a new user using Session.persist()")
    void shouldSaveNewUserUsingTransaction() {
        Users newUser = new Users();
        newUser.setFirstName("new First Name");
        newUser.setLastName("User");

        log.info("::: shouldSaveNewUser ::: Calling session.save(). INSERT query will be sent when transaction commits.");
        Transaction transaction = session.beginTransaction(); // Begin transaction to ensure changes are committed
        session.persist(newUser); // Use persist to add the new user to the session
        transaction.commit(); // Commit the transaction to persist the new user

        assertNotNull(newUser.getId());
        assertThat(newUser.getId()).isGreaterThan(0);

        try (Session verificationSession = sessionFactory.openSession()) {
            Users foundUser = verificationSession.get(Users.class, newUser.getId());
            assertNotNull(foundUser);
            assertEquals(newUser.getFirstName(), foundUser.getFirstName());
        }
    }

    @Test
    @DisplayName("Should update an existing user using Session.update()")
    void shouldUpdateExistingUsers() {

        Users userToUpdate = session.get(Users.class, USER_ID_FOR_UPDATE);

        userToUpdate.setFirstName("UpdatedFirstName");
        userToUpdate.setLastName("UpdatedLastName");

        log.info("::: shouldUpdateExistingUserDirectly ::: Calling session.update(). UPDATE query will be sent when transaction commits.");

        Transaction transaction = session.beginTransaction(); // Begin transaction to ensure changes are committed
        session.merge(userToUpdate);  // Use update to modify the existing user in the session
        transaction.commit(); // Commit the transaction to persist the changes

        try (Session verificationSession = sessionFactory.openSession()) {
            Users verifiedUser = verificationSession.get(Users.class, USER_ID_FOR_UPDATE);
            assertNotNull(verifiedUser);
            assertEquals("UpdatedFirstName", verifiedUser.getFirstName());
        }
    }

    @Test
    @DisplayName("Should delete an existing user using Session.delete()")
    void shouldDeleteExistingUserDirectly() {
        Users userToDelete = session.get(Users.class, USER_ID_FOR_DELETE);
        assertNotNull(userToDelete);

        log.info("::: shouldDeleteExistingUserDirectly ::: Calling session.delete(). DELETE query will be sent when transaction commits.");
        Transaction transaction = session.beginTransaction(); // Begin transaction to ensure changes are committed
        session.remove(userToDelete); // Use remove to delete the user from the session
        transaction.commit(); // Commit the transaction to delete the user

        try (Session verificationSession = sessionFactory.openSession()) {
            Users deletedUser = verificationSession.get(Users.class, USER_ID_FOR_DELETE);
            assertNull(deletedUser);
            log.info("::: shouldDeleteExistingUserDirectly ::: User with ID {} successfully deleted.", USER_ID_FOR_DELETE);
        }
    }
}