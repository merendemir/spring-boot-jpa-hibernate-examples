package com.trio.spring.boot.jpa.hibernate.example.service;

import com.trio.spring.boot.jpa.hibernate.example.converter.UserConverter;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserRequest;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserResponse;
import com.trio.spring.boot.jpa.hibernate.example.repository.domain.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntityManagerOperationsExampleServiceTest {

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction entityTransaction;

    @Mock
    private UserConverter userConverter;

    @Mock
    private TypedQuery<Users> typedQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Users> criteriaQuery;

    @Mock
    private Root<Users> root;

    @InjectMocks
    private EntityManagerOperationsExampleService service;

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
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.find(Users.class, 1L)).thenReturn(testUser);
        when(userConverter.toResponse(testUser)).thenReturn(userResponse);

        UserResponse result = service.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        verify(entityManagerFactory).createEntityManager();
        verify(entityManager).find(Users.class, 1L);
        verify(entityManager).close();
        verify(userConverter).toResponse(testUser);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.find(Users.class, 1L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> service.getUserById(1L));
        verify(entityManagerFactory).createEntityManager();
        verify(entityManager).find(Users.class, 1L);
        verify(entityManager).close();
    }

    @Test
    void shouldSaveUser() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(userConverter.toEntity(userRequest)).thenReturn(testUser);

        Users result = service.saveUser(userRequest);

        assertNotNull(result);
        verify(userConverter).toEntity(userRequest);
        verify(entityManagerFactory).createEntityManager();
        verify(entityTransaction).begin();
        verify(entityManager).persist(testUser);
        verify(entityTransaction).commit();
        verify(entityManager).close();
    }

    @Test
    void shouldUpdateUser() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(Users.class, 1L)).thenReturn(testUser);
        when(userConverter.updateEntity(testUser, userRequest)).thenReturn(testUser);

        Users result = service.updateUser(1L, userRequest);

        assertNotNull(result);
        verify(entityManagerFactory, times(2)).createEntityManager();
        verify(entityManager).find(Users.class, 1L);
        verify(userConverter).updateEntity(testUser, userRequest);
        verify(entityTransaction).begin();
        verify(entityManager).merge(testUser);
        verify(entityTransaction).commit();
        verify(entityManager, times(2)).close();
    }

    @Test
    void shouldDeleteUser() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.find(Users.class, 1L)).thenReturn(testUser);

        service.deleteUser(1L);

        verify(entityManagerFactory).createEntityManager();
        verify(entityTransaction).begin();
        verify(entityManager).find(Users.class, 1L);
        verify(entityManager).remove(testUser);
        verify(entityTransaction).commit();
        verify(entityManager).close();
    }

    @Test
    void shouldFindAllUsers() {
        List<Users> usersList = Arrays.asList(testUser);
        List<UserResponse> responseList = Arrays.asList(userResponse);

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery("SELECT u FROM Users u", Users.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(usersList);
        when(userConverter.toResponseList(usersList)).thenReturn(responseList);

        List<UserResponse> result = service.findAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(entityManagerFactory).createEntityManager();
        verify(entityManager).createQuery("SELECT u FROM Users u", Users.class);
        verify(entityManager).close();
        verify(userConverter).toResponseList(usersList);
    }

    @Test
    void shouldFindAllBySearchParam() {
        List<Users> usersList = Arrays.asList(testUser);
        List<UserResponse> responseList = Arrays.asList(userResponse);

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Users.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Users.class)).thenReturn(root);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(criteriaQuery.where(any(jakarta.persistence.criteria.Predicate[].class))).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(usersList);
        when(userConverter.toResponseList(usersList)).thenReturn(responseList);

        List<UserResponse> result = service.findAllBySearchParam("John");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(entityManagerFactory).createEntityManager();
        verify(entityManager).getCriteriaBuilder();
        verify(entityManager).close();
        verify(userConverter).toResponseList(usersList);
    }
}