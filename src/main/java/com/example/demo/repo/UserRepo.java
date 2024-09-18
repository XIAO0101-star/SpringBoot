package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.User;


public interface UserRepo extends JpaRepository<User, Long> {
    // automated query generation
    // wwill convert based on the method name to:
    // SELECT * from user_accounts WHERE username=:?
    User findByUsername(String username);
}
