package com.trio.spring.boot.jpa.hibernate.example.service;

import com.trio.spring.boot.jpa.hibernate.example.converter.UserConverter;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserRequest;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserResponse;
import com.trio.spring.boot.jpa.hibernate.example.repository.domain.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HibernateSessionExampleServiceTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private UserConverter userConverter;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private Query<Users> query;

    @InjectMocks
    private HibernateSessionExampleService service;

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
    void shouldGetUserById() {
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.get(Users.class, 1L)).thenReturn(testUser);
        when(userConverter.toResponse(testUser)).thenReturn(userResponse);

        UserResponse result = service.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        verify(sessionFactory).openSession();
        verify(session).get(Users.class, 1L);
        verify(userConverter).toResponse(testUser);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.get(Users.class, 1L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> service.getUserById(1L));
        verify(sessionFactory).openSession();
        verify(session).get(Users.class, 1L);
    }

    @Test
    void shouldSaveUser() {
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        when(userConverter.toEntity(userRequest)).thenReturn(testUser);

        Users result = service.saveUser(userRequest);

        assertNotNull(result);
        verify(userConverter).toEntity(userRequest);
        verify(sessionFactory).openSession();
        verify(session).beginTransaction();
        verify(session).persist(testUser);
        verify(transaction).commit();
    }

    @Test
    void shouldUpdateUser() {
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.get(Users.class, 1L)).thenReturn(testUser);
        when(userConverter.updateEntity(testUser, userRequest)).thenReturn(testUser);

        Users result = service.updateUser(1L, userRequest);

        assertNotNull(result);
        verify(sessionFactory, times(2)).openSession();
        verify(session).get(Users.class, 1L);
        verify(userConverter).updateEntity(testUser, userRequest);
        verify(session).merge(testUser);
        verify(transaction).commit();
    }

    @Test
    void shouldDeleteUser() {
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.get(Users.class, 1L)).thenReturn(testUser);

        service.deleteUser(1L);

        verify(sessionFactory, times(1)).openSession();
        verify(session).get(Users.class, 1L);
        verify(session).remove(testUser);
        verify(transaction).commit();
    }

    @Test
    void shouldFindAllUsers() {
        List<Users> usersList = Collections.singletonList(testUser);
        List<UserResponse> responseList = Collections.singletonList(userResponse);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.createQuery("FROM Users", Users.class)).thenReturn(query);
        when(query.list()).thenReturn(usersList);
        when(userConverter.toResponseList(usersList)).thenReturn(responseList);

        List<UserResponse> result = service.findAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sessionFactory).openSession();
        verify(session).createQuery("FROM Users", Users.class);
        verify(userConverter).toResponseList(usersList);
    }

    @Test
    void shouldRollbackOnSaveError() {
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        when(userConverter.toEntity(userRequest)).thenReturn(testUser);
        doThrow(new RuntimeException("Database error")).when(session).persist(testUser);

        assertThrows(RuntimeException.class, () -> service.saveUser(userRequest));
        verify(transaction).rollback();
    }
}