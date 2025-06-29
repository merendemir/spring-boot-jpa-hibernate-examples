package com.trio.spring.boot.jpa.hibernate.example.service;

import com.trio.spring.boot.jpa.hibernate.example.converter.UserConverter;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserRequest;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserResponse;
import com.trio.spring.boot.jpa.hibernate.example.repository.UsersRepository;
import com.trio.spring.boot.jpa.hibernate.example.repository.domain.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringDataJpaExampleServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private SpringDataJpaExampleService service;

    private Users testUser;
    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        userRequest = new UserRequest();
        userRequest.setFirstName("John");
        userRequest.setLastName("Doe");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setFirstName("John");
        userResponse.setLastName("Doe");
    }

    @Test
    void shouldSaveUser() {
        when(userConverter.toEntity(userRequest)).thenReturn(testUser);
        when(usersRepository.save(testUser)).thenReturn(testUser);

        Users result = service.saveUser(userRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userConverter).toEntity(userRequest);
        verify(usersRepository).save(testUser);
    }

    @Test
    void shouldUpdateUser() {
        when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userConverter.updateEntity(testUser, userRequest)).thenReturn(testUser);
        when(usersRepository.save(testUser)).thenReturn(testUser);

        Users result = service.updateUser(1L, userRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(usersRepository).findById(1L);
        verify(userConverter).updateEntity(testUser, userRequest);
        verify(usersRepository).save(testUser);
    }

    @Test
    void shouldDeleteUser() {
        when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));

        service.deleteUser(1L);

        verify(usersRepository).findById(1L);
        verify(usersRepository).delete(testUser);
    }

    @Test
    void shouldFindAllUsers() {
        List<Users> usersList = Arrays.asList(testUser);
        List<UserResponse> responseList = Arrays.asList(userResponse);

        when(usersRepository.findAll()).thenReturn(usersList);
        when(userConverter.toResponseList(usersList)).thenReturn(responseList);

        List<UserResponse> result = service.findAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(usersRepository).findAll();
        verify(userConverter).toResponseList(usersList);
    }

    @Test
    void shouldGetById() {
        when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userConverter.toResponse(testUser)).thenReturn(userResponse);

        UserResponse result = service.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        verify(usersRepository).findById(1L);
        verify(userConverter).toResponse(testUser);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(usersRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getById(1L));
        verify(usersRepository).findById(1L);
    }
}