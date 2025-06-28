package com.trio.spring.boot.jpa.hibernate.example.jpa;

import com.trio.spring.boot.jpa.hibernate.example.repository.UsersRepository;
import com.trio.spring.boot.jpa.hibernate.example.repository.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
@DisplayName("JPA Query Tests")
public class JpaQueryTests {

    // This class will contain tests for JPA queries using method name query derivation and JPQL.
// It will test the retrieval of users by their first and last names.

    @Autowired
    private UsersRepository usersRepository;

    @Test
    @DisplayName("Should find users by first name using method name query derivation")
    void shouldFindUsersByFirstName() {
        String firstName = "Alice";

        // findByFirstName is a method in UsersRepository that retrieves users by their first name
        // it generates a query like:  SELECT * FROM users WHERE first_name = ?
        log.info("::: shouldFindUsersByFirstName :::  Searching for users with first name: {}", firstName);
        List<Users> usersList = usersRepository.findByFirstName(firstName);

        assertThat(usersList).isNotEmpty();
    }

    @Test
    @DisplayName("Should find last name using Java Persistence Query Language")
    void shouldFindLastNameUsingJPQL() {
        String lastName = "Smith";
        log.info("Searching for users with last name: {}", lastName);

        // findByLastName is a method in UsersRepository that retrieves users by their last name using JPQL
        // it generates a query like:  SELECT u FROM Users u WHERE u.lastName = :lastName
        log.info("::: shouldFindLastNameUsingJPQL :::  Searching for users with last name: {}", lastName);
        List<Users> usersList = usersRepository.findByLastName(lastName);

        assertThat(usersList).isNotEmpty();
    }



}
