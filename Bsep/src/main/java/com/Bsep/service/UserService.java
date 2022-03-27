package com.Bsep.service;

import java.util.List;


import com.Bsep.model.User;

public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll ();
}
