package com.trio.spring.boot.jpa.hibernate.example.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
}
