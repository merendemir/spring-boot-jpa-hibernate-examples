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

@Tag(name = "Entity Manager", description = "CRUD operations using JPA Entity Manager")
public interface EntityManagerOperationsExampleApi {

    @Operation(
            summary = "Get all users",
            description = "Retrieves all users from database using Entity Manager"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Users retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    ResponseEntity<List<UserResponse>> getUsers();

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a user by specified ID using Entity Manager"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User found successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID", required = true, example = "1")
            Long userId
    );

    @Operation(
            summary = "Create new user",
            description = "Creates a new user in database using Entity Manager"
    )
    @ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)
            )
    )
    ResponseEntity<Long> createUser(
            @Parameter(description = "User information to create", required = true)
            UserRequest userRequest
    );

    @Operation(
            summary = "Update user",
            description = "Updates existing user information using Entity Manager"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)
            )
    )
    ResponseEntity<Long> updateUser(
            @Parameter(description = "ID of user to update", required = true, example = "1")
            Long userId,
            @Parameter(description = "Updated user information", required = true)
            UserRequest userRequest
    );

    @Operation(
            summary = "Delete user",
            description = "Deletes user with specified ID using Entity Manager"
    )
    @ApiResponse(
            responseCode = "204",
            description = "User deleted successfully"
    )
    ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of user to delete", required = true, example = "1")
            Long userId
    );

    @Operation(
            summary = "Search users",
            description = "Searches users by first name or last name using Entity Manager"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search results retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    ResponseEntity<List<UserResponse>> searchUsers(
            @Parameter(description = "Search term (first name or last name)", required = true, example = "John")
            String searchParam
    );
}