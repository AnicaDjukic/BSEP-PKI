package com.Bsep.dto;


public class TokenDTO {
    private String jwt;
    private String role;

    public TokenDTO(String jwt, String role) {
        this.jwt = jwt;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getJwt() {
        return jwt;
    }
}
