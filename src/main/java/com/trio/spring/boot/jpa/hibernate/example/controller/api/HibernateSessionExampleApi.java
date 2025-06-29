package com.trio.spring.boot.jpa.hibernate.example.controller.api;

import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserRequest;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Hibernate Session", description = "CRUD operations using Hibernate Session API")
public interface HibernateSessionExampleApi {

    @Operation(
            summary = "Get all users",
            description = "Retrieves all users from database using Hibernate Session"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Users retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    @GetMapping
    ResponseEntity<List<UserResponse>> getUsers();

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a user by specified ID using Hibernate Session"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User found successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    @GetMapping("/{userId}")
    ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long userId
    );

    @Operation(
            summary = "Create new user",
            description = "Creates a new user in database using Hibernate Session"
    )
    @ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)
            )
    )
    @PostMapping
    ResponseEntity<Long> createUser(
            @Parameter(description = "User information to create", required = true)
            @RequestBody UserRequest userRequest
    );

    @Operation(
            summary = "Update user",
            description = "Updates existing user information using Hibernate Session"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)
            )
    )
    @PutMapping("/{userId}")
    ResponseEntity<Long> updateUser(
            @Parameter(description = "ID of user to update", required = true, example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Updated user information", required = true)
            @RequestBody UserRequest userRequest
    );

    @Operation(
            summary = "Delete user",
            description = "Deletes user with specified ID using Hibernate Session"
    )
    @ApiResponse(
            responseCode = "204",
            description = "User deleted successfully"
    )
    @DeleteMapping("/{userId}")
    ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of user to delete", required = true, example = "1")
            @PathVariable Long userId
    );
}