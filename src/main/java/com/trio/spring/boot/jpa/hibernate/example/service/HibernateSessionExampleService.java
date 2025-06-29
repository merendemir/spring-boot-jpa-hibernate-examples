package com.trio.spring.boot.jpa.hibernate.example.service;

import com.trio.spring.boot.jpa.hibernate.example.converter.UserConverter;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserRequest;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserResponse;
import com.trio.spring.boot.jpa.hibernate.example.repository.domain.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HibernateSessionExampleService {

    // This class performs CRUD operations for Users entity using Hibernate session

    private final SessionFactory sessionFactory;
    private final UserConverter userConverter;

    public UserResponse getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        Users user = findUserById(id);
        return userConverter.toResponse(user);
    }

    public Users saveUser(UserRequest userRequest) {
        log.info("Saving user with request: {}", userRequest);
        Users user = userConverter.toEntity(userRequest);
        try (Session session = sessionFactory.openSession()) { // Open Hibernate Session from SessionFactory
            Transaction transaction = session.beginTransaction(); // Begin transaction for database operations
            try {
                session.persist(user); // Use Session.persist() to save new entity to session
                transaction.commit(); // Commit transaction to persist changes to database
                log.info("User saved with ID: {}", user.getId());
                return user;
            } catch (Exception e) {
                transaction.rollback(); // Rollback transaction on error
                throw e;
            }
        }
    }

    public Users updateUser(Long id, UserRequest userRequest) {
        log.info("Updating user with ID: {} and request: {}", id, userRequest);
        Users user = findUserById(id);
        Users updatedUser = userConverter.updateEntity(user, userRequest);
        try (Session session = sessionFactory.openSession()) { // Open Hibernate Session from SessionFactory
            Transaction transaction = session.beginTransaction(); // Begin transaction for database operations
            try {
                session.merge(updatedUser); // Use Session.merge() to update existing entity in session
                transaction.commit(); // Commit transaction to persist changes to database
                log.info("User updated with ID: {}", updatedUser.getId());
                return updatedUser;
            } catch (Exception e) {
                transaction.rollback(); // Rollback transaction on error
                throw e;
            }
        }
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        try (Session session = sessionFactory.openSession()) { // Open Hibernate Session from SessionFactory
            Transaction transaction = session.beginTransaction(); // Begin transaction for database operations
            try {
                Users user = session.get(Users.class, id); // Get entity within the same session
                if (user != null) {
                    session.remove(user); // Use Session.remove() to delete entity from session
                }
                transaction.commit(); // Commit transaction to persist changes to database
                log.info("User deleted with ID: {}", id);
            } catch (Exception e) {
                transaction.rollback(); // Rollback transaction on error
                throw e;
            }
        }
    }

    public List<UserResponse> findAllUsers() {
        log.info("Fetching all users");
        try (Session session = sessionFactory.openSession()) { // Open Hibernate Session from SessionFactory
            List<Users> users = session.createQuery("FROM Users", Users.class).list(); // Use Session.createQuery() to execute HQL query
            return userConverter.toResponseList(users);
        }
    }

    private Users findUserById(Long id) {
        log.info("Finding user with ID: {}", id);
        try (Session session = sessionFactory.openSession()) { // Open Hibernate Session from SessionFactory
            return Optional.ofNullable(session.get(Users.class, id)) // Use Session.get() to retrieve entity by primary key
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        }
    }
}