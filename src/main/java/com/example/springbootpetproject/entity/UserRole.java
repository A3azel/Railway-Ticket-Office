package com.example.springbootpetproject.entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    S, USER,ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
