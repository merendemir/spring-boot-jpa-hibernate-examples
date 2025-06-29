package com.trio.spring.boot.jpa.hibernate.example.controller;

import com.trio.spring.boot.jpa.hibernate.example.controller.api.EntityManagerOperationsExampleApi;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserRequest;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserResponse;
import com.trio.spring.boot.jpa.hibernate.example.service.EntityManagerOperationsExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/entity-manager")
public class EntityManagerOperationsExampleController implements EntityManagerOperationsExampleApi {

    private final EntityManagerOperationsExampleService entityManagerOperationsExampleService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(entityManagerOperationsExampleService.findAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(entityManagerOperationsExampleService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entityManagerOperationsExampleService.saveUser(userRequest).getId());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Long> updateUser(@PathVariable Long userId, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(entityManagerOperationsExampleService.updateUser(userId, userRequest).getId());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        entityManagerOperationsExampleService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String searchParam) {
        return ResponseEntity.ok(entityManagerOperationsExampleService.findAllBySearchParam(searchParam));
    }
}