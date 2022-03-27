package com.Bsep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Bsep.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

