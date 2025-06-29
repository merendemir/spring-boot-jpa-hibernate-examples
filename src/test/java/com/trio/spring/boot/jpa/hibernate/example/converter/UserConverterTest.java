package com.trio.spring.boot.jpa.hibernate.example.converter;

import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserRequest;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserResponse;
import com.trio.spring.boot.jpa.hibernate.example.repository.domain.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

    private UserConverter userConverter;
    private Users testUser;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userConverter = new UserConverter();

        testUser = new Users();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        userRequest = new UserRequest();
        userRequest.setFirstName("Jane");
        userRequest.setLastName("Smith");
    }

    @Test
    void shouldConvertUserRequestToEntity() {
        Users result = userConverter.toEntity(userRequest);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertNull(result.getId());
    }

    @Test
    void shouldReturnNullWhenUserRequestIsNull() {
        Users result = userConverter.toEntity(null);

        assertNull(result);
    }

    @Test
    void shouldUpdateExistingEntity() {
        Users result = userConverter.updateEntity(testUser, userRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
    }

    @Test
    void shouldReturnOriginalUserWhenUserRequestIsNull() {
        Users result = userConverter.updateEntity(testUser, null);

        assertEquals(testUser, result);
    }

    @Test
    void shouldReturnNullWhenUserIsNull() {
        Users result = userConverter.updateEntity(null, userRequest);

        assertNull(result);
    }

    @Test
    void shouldConvertEntityToResponse() {
        UserResponse result = userConverter.toResponse(testUser);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        UserResponse result = userConverter.toResponse(null);

        assertNull(result);
    }

    @Test
    void shouldConvertEntityListToResponseList() {
        List<Users> usersList = Arrays.asList(testUser);

        List<UserResponse> result = userConverter.toResponseList(usersList);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void shouldReturnEmptyListWhenUsersListIsNull() {
        List<UserResponse> result = userConverter.toResponseList(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenUsersListIsEmpty() {
        List<UserResponse> result = userConverter.toResponseList(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}