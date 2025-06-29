package com.trio.spring.boot.jpa.hibernate.example.converter;

import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserRequest;
import com.trio.spring.boot.jpa.hibernate.example.data.dto.UserResponse;
import com.trio.spring.boot.jpa.hibernate.example.repository.domain.Users;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserConverter {

    public Users toEntity(UserRequest userRequest) {
        if (userRequest == null) {
            return null;
        }

        Users user = new Users();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());

        return user;
    }

    public Users updateEntity(Users user, UserRequest userRequest) {
        if (user == null || userRequest == null) {
            return user;
        }

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());

        return user;
    }

    public List<UserResponse> toResponseList(List<Users> users) {
        if (users == null || users.isEmpty()) {
            return List.of();
        }

        return users.stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse toResponse(Users user) {
        if (user == null) {
            return null;
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        return userResponse;
    }
}
