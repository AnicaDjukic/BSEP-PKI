package com.Bsep.service;

import com.Bsep.dto.TokenDTO;

public interface AuthenticationService {
    TokenDTO login(String username, String password);
}
