package com.trio.spring.boot.jpa.hibernate.example.controller;

import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserRequest;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserResponse;
import com.trio.spring.boot.jpa.hibernate.example.service.SpringDataJpaExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/spring-data-jpa")
public class SpringDataJpaExampleController {

    private final SpringDataJpaExampleService springDataJpaExampleService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(springDataJpaExampleService.findAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(springDataJpaExampleService.getById(userId));
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(springDataJpaExampleService.saveUser(userRequest).getId());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Long> updateUser(@PathVariable Long userId, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(springDataJpaExampleService.updateUser(userId, userRequest).getId());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        springDataJpaExampleService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
