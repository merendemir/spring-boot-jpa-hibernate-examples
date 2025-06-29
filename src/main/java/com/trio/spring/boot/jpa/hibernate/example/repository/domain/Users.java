package com.trio.spring.boot.jpa.hibernate.example.repository.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity // This annotation marks the class as a JPA entity
@Table(name = "users") // This annotation specifies the table name in the database
public class Users {

    @Id // This annotation specifies the primary key of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This annotation specifies the generation strategy for the primary key
    @Column(name = "id", nullable = false) // This annotation specifies the column name and constraints
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
}
