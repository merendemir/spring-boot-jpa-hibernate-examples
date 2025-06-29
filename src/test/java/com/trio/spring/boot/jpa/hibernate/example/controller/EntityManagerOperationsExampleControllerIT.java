package com.trio.spring.boot.jpa.hibernate.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserRequest;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Transactional
@Sql(scripts = "classpath:sql/init_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:sql/cleanup_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class EntityManagerOperationsExampleControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private String createUrl(String path) {
        return "http://localhost:" + port + "/api/v1/users/entity-manager" + path;
    }

    @Test
    void shouldGetAllUsers() {
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                createUrl(""), 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<UserResponse>>(){});
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 3);
        
        UserResponse firstUser = response.getBody().stream()
                .filter(user -> user.getId().equals(1000L))
                .findFirst()
                .orElse(null);
        
        assertNotNull(firstUser);
        assertEquals("Alice", firstUser.getFirstName());
        assertEquals("Smith", firstUser.getLastName());
    }

    @Test
    void shouldGetUserById() {
        ResponseEntity<UserResponse> response = restTemplate.getForEntity(createUrl("/1000"), UserResponse.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1000L, response.getBody().getId());
        assertEquals("Alice", response.getBody().getFirstName());
        assertEquals("Smith", response.getBody().getLastName());
    }

    @Test
    void shouldCreateUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("John");
        userRequest.setLastName("Doe");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);

        ResponseEntity<Long> response = restTemplate.postForEntity(createUrl(""), request, Long.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() > 0);
    }

    @Test
    void shouldUpdateUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("Updated");
        userRequest.setLastName("User");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRequest> request = new HttpEntity<>(userRequest, headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                createUrl("/1002"), 
                HttpMethod.PUT, 
                request, 
                Long.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1002L, response.getBody());
    }

    @Test
    void shouldDeleteUser() {
        ResponseEntity<Void> response = restTemplate.exchange(
                createUrl("/1001"), 
                HttpMethod.DELETE, 
                null, 
                Void.class);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldSearchUsers() {
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                createUrl("/search?searchParam=Alice"), 
                HttpMethod.GET, 
                null,
                new ParameterizedTypeReference<>() {
                });
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(!response.getBody().isEmpty());
        
        UserResponse foundUser = response.getBody().get(0);
        assertTrue(foundUser.getFirstName().contains("Alice") || foundUser.getLastName().contains("Alice"));
    }
}