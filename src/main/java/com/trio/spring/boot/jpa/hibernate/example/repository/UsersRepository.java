package com.trio.spring.boot.jpa.hibernate.example.repository;

import com.trio.spring.boot.jpa.hibernate.example.repository.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {

    List<Users> findByFirstName(String firstName);

    @Query("SELECT u FROM Users u WHERE u.lastName = :lastName")
    List<Users> findByLastName(String lastName);
}
