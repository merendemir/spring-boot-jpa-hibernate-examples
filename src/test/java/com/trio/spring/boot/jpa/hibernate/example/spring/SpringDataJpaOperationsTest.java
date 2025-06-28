package com.trio.spring.boot.jpa.hibernate.example.spring;

import com.trio.spring.boot.jpa.hibernate.example.repository.UsersRepository;
import com.trio.spring.boot.jpa.hibernate.example.repository.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

// @Sql annotation to execute SQL scripts before and after the test class.
// We are pointing to SQL files in src/test/resources.
@Sql(
        scripts = {"classpath:sql/init_users.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Sql(
        scripts = "classpath:sql/cleanup_users.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS
)
@DataJpaTest // Loads JPA components and configures a test database
@Slf4j
@DisplayName("Spring Data JPA Operations Tests")
public class SpringDataJpaOperationsTest {

    // This class will contain tests for Spring Data JPA operations
    // such as saving, updating, deleting, and retrieving entities using Spring Data JPA repositories.

    private final Long USER_ID_FOR_RETRIEVE = 1000L;
    private final Long USER_ID_FOR_DELETE = 1001L;
    private final Long USER_ID_FOR_UPDATE = 1002L;
    private final String RETRIEVE_USER_FIRST_NAME = "Alice";
    private final String RETRIEVE_USER_LAST_NAME = "Smith";

    @Autowired
    private UsersRepository usersRepository;


    @Test
    @DisplayName("Should find an existing user by ID using Spring Data JPA")
    void shouldFindExistingUserById() {
        log.info("::: shouldFindExistingUserById ::: Retrieving user with ID: {}", USER_ID_FOR_RETRIEVE);
        Users foundUser = usersRepository.findById(USER_ID_FOR_RETRIEVE).orElse(null); // Use findById to retrieve the user by ID

        assertNotNull(foundUser, "User should not be null");
        assertEquals(USER_ID_FOR_RETRIEVE, foundUser.getId(), "User ID should match");
        assertEquals(RETRIEVE_USER_FIRST_NAME, foundUser.getFirstName(), "User first name should match");
        assertEquals(RETRIEVE_USER_LAST_NAME, foundUser.getLastName(), "User last name should match");

    }

    @Test
    @DisplayName("Should save a new user using Spring Data JPA")
    void shouldSaveNewUser() {
        Users newUser = new Users();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");

        log.info("::: shouldSaveNewUser ::: Saving new user: {}", newUser);
        Users savedUser = usersRepository.save(newUser); // Use save to persist the new user

        assertNotNull(savedUser, "Saved user should not be null");
        assertNotNull(savedUser.getId(), "Saved user ID should not be null");
        assertEquals(newUser.getFirstName(), savedUser.getFirstName(), "First name should match");
        assertEquals(newUser.getLastName(), savedUser.getLastName(), "Last name should match");
    }

    @Test
    @DisplayName("Should update an existing user using Spring Data JPA")
    void shouldUpdateExistingUser() {
        log.info("::: shouldUpdateExistingUser ::: Updating user with ID: {}", USER_ID_FOR_UPDATE);
        Users existingUser = usersRepository.findById(USER_ID_FOR_UPDATE).orElse(null);

        assertNotNull(existingUser, "Existing user should not be null");

        existingUser.setFirstName("Updated First Name");
        existingUser.setLastName("Updated Last Name");

        Users updatedUser = usersRepository.save(existingUser); // Use save to update the existing user

        assertNotNull(updatedUser, "Updated user should not be null");
        assertEquals("Updated First Name", updatedUser.getFirstName(), "First name should be updated");
        assertEquals("Updated Last Name", updatedUser.getLastName(), "Last name should be updated");
    }

    @Test
    @DisplayName("Should delete an existing user using Spring Data JPA")
    void shouldDeleteExistingUser() {
        log.info("::: shouldDeleteExistingUser ::: Deleting user with ID: {}", USER_ID_FOR_DELETE);
        Users existingUser = usersRepository.findById(USER_ID_FOR_DELETE).orElse(null);

        assertNotNull(existingUser, "Existing user should not be null");

        usersRepository.delete(existingUser); // Use delete to remove the existing user

        Users deletedUser = usersRepository.findById(USER_ID_FOR_DELETE).orElse(null);
        assertNull(deletedUser, "User should be deleted and not found");
    }
}
