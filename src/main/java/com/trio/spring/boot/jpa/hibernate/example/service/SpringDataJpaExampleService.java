package com.trio.spring.boot.jpa.hibernate.example.service;

import com.trio.spring.boot.jpa.hibernate.example.converter.UserConverter;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserRequest;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserResponse;
import com.trio.spring.boot.jpa.hibernate.example.repository.UsersRepository;
import com.trio.spring.boot.jpa.hibernate.example.repository.domain.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpringDataJpaExampleService {

    // This class performs crud operations for Users entity using Spring Data JPA

    private final UsersRepository usersRepository;
    private final UserConverter userConverter;

    public Users saveUser(UserRequest userRequest) {
        log.info("Saving user with request: {}", userRequest);
        Users user = userConverter.toEntity(userRequest); // Convert UserRequest to Users entity
        Users savedUser = usersRepository.save(user); // Use Spring Data JPA repository.save() to persist entity to database
        log.info("User saved with ID: {}", savedUser.getId());
        return savedUser;
    }

    public Users updateUser(Long id, UserRequest userRequest) {
        log.info("Updating user with ID: {} and request: {}", id, userRequest);
        Users existingUser = findUserById(id);
        Users updatedUser = userConverter.updateEntity(existingUser, userRequest); // Update the existing Users entity with new data from UserRequest
        Users savedUser = usersRepository.save(updatedUser); // Use Spring Data JPA repository.save() to update existing entity in database
        log.info("User updated with ID: {}", savedUser.getId());
        return savedUser;
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        Users user = findUserById(id);
        usersRepository.delete(user); // Use Spring Data JPA repository.delete() to remove entity from database
        log.info("User deleted with ID: {}", id);
    }

    public List<UserResponse> findAllUsers() {
        log.info("Fetching all users");
        List<Users> users = usersRepository.findAll(); // Use Spring Data JPA repository.findAll() to retrieve all entities from database
        return userConverter.toResponseList(users);
    }

    public UserResponse getById(Long id) {
        log.info("Fetching user response with ID: {}", id);
        Users user = findUserById(id);
        return userConverter.toResponse(user);
    }

    private Users findUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        return usersRepository.findById(id)  // Use Spring Data JPA repository.findById() to retrieve entity by primary key
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }
}
