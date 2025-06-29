package com.trio.spring.boot.jpa.hibernate.example.controller;

import com.trio.spring.boot.jpa.hibernate.example.controller.api.HibernateSessionExampleApi;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserRequest;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserResponse;
import com.trio.spring.boot.jpa.hibernate.example.service.HibernateSessionExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/hibernate-session")
public class HibernateSessionExampleController implements HibernateSessionExampleApi {

    private final HibernateSessionExampleService hibernateSessionExampleService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(hibernateSessionExampleService.findAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(hibernateSessionExampleService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hibernateSessionExampleService.saveUser(userRequest).getId());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Long> updateUser(@PathVariable Long userId, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(hibernateSessionExampleService.updateUser(userId, userRequest).getId());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        hibernateSessionExampleService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}