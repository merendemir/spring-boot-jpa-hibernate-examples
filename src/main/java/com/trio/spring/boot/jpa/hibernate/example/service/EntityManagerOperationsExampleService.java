package com.trio.spring.boot.jpa.hibernate.example.service;

import com.trio.spring.boot.jpa.hibernate.example.converter.UserConverter;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserRequest;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserResponse;
import com.trio.spring.boot.jpa.hibernate.example.repository.UsersRepository;
import com.trio.spring.boot.jpa.hibernate.example.repository.domain.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EntityManagerOperationsExampleService {

    // This class performs CRUD operations for Users entity using EntityManager

    private final EntityManagerFactory entityManagerFactory;
    private final UserConverter userConverter;

    public UserResponse getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        Users user = findUserById(id);
        return userConverter.toResponse(user);
    }

    public Users saveUser(UserRequest userRequest) {
        log.info("Saving user with request: {}", userRequest);
        Users user = userConverter.toEntity(userRequest);
        EntityManager em = entityManagerFactory.createEntityManager(); // Create EntityManager from EntityManagerFactory
        try {
            em.getTransaction().begin(); // Begin EntityManager transaction
            em.persist(user); // Use EntityManager.persist() to save new entity to persistence context
            em.getTransaction().commit(); // Commit EntityManager transaction
            log.info("User saved with ID: {}", user.getId());
            return user;
        } catch (Exception e) {
            em.getTransaction().rollback(); // Rollback EntityManager transaction on error
            throw e;
        } finally {
            em.close(); // Close EntityManager
        }
    }

    public Users updateUser(Long id, UserRequest userRequest) {
        log.info("Updating user with ID: {} and request: {}", id, userRequest);
        Users user = findUserById(id);
        Users updatedUser = userConverter.updateEntity(user, userRequest);
        EntityManager em = entityManagerFactory.createEntityManager(); // Create EntityManager from EntityManagerFactory
        try {
            em.getTransaction().begin(); // Begin EntityManager transaction
            em.merge(updatedUser); // Use EntityManager.merge() to update existing entity in persistence context
            em.getTransaction().commit(); // Commit EntityManager transaction
            log.info("User updated with ID: {}", updatedUser.getId());
            return updatedUser;
        } catch (Exception e) {
            em.getTransaction().rollback(); // Rollback EntityManager transaction on error
            throw e;
        } finally {
            em.close(); // Close EntityManager
        }
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager(); // Create EntityManager from EntityManagerFactory
        try {
            em.getTransaction().begin(); // Begin EntityManager transaction
            Users user = em.find(Users.class, id); // Find entity within the same EntityManager
            if (user != null) {
                em.remove(user); // Use EntityManager.remove() to delete entity from persistence context
            }
            em.getTransaction().commit(); // Commit EntityManager transaction
            log.info("User deleted with ID: {}", id);
        } catch (Exception e) {
            em.getTransaction().rollback(); // Rollback EntityManager transaction on error
            throw e;
        } finally {
            em.close(); // Close EntityManager
        }
    }

    public List<UserResponse> findAllUsers() {
        log.info("Fetching all users");
        EntityManager em = entityManagerFactory.createEntityManager(); // Create EntityManager from EntityManagerFactory
        try {
            List<Users> users = em.createQuery("SELECT u FROM Users u", Users.class).getResultList(); // Use EntityManager.createQuery() to execute JPQL query
            return userConverter.toResponseList(users);
        } finally {
            em.close(); // Close EntityManager
        }
    }

    public List<UserResponse> findAllBySearchParam(String searchParam) {
        EntityManager em = entityManagerFactory.createEntityManager(); // Create EntityManager from EntityManagerFactory
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder(); // CriteriaBuilder is used to construct criteria queries, compound selections, expressions, predicates, etc.
            CriteriaQuery<Users> query = criteriaBuilder.createQuery(Users.class); // CriteriaQuery is used to define the query structure and specify the result type.

            // Specify the root entity
            Root<Users> root = query.from(Users.class);
            List<Predicate> predicates = new ArrayList<>();

            if (searchParam != null && !searchParam.isEmpty()) {
                Predicate firstNamePredicate = criteriaBuilder.like(root.get("firstName"), "%" + searchParam + "%"); // Create a predicate for first name search
                Predicate lastNamePredicate = criteriaBuilder.like(root.get("lastName"), "%" + searchParam + "%"); // Create a predicate for last name search

                predicates.add(criteriaBuilder.or(firstNamePredicate, lastNamePredicate)); // Combine predicates with OR logic
            }

            query.select(root).where(predicates.toArray(new Predicate[0])); // Apply the predicates to the query

            List<Users> resultList = em.createQuery(query).getResultList(); // Use EntityManager.createQuery() to execute criteria query and get result list

            return userConverter.toResponseList(resultList);
        } finally {
            em.close(); // Close EntityManager
        }
    }

    private Users findUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        EntityManager em = entityManagerFactory.createEntityManager(); // Create EntityManager from EntityManagerFactory
        try {
            return Optional.ofNullable(em.find(Users.class, id)) // Use EntityManager.find() to retrieve entity by primary key
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        } finally {
            em.close(); // Close EntityManager
        }
    }


}
