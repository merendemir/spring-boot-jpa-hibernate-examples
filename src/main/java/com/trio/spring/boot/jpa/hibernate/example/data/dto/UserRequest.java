package com.trio.spring.boot.jpa.hibernate.example.data.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequest {
    private String firstName;
    private String lastName;
}
