package com.ecom.userservice.security.models;

import com.ecom.userservice.models.Role;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.core.GrantedAuthority;

@JsonDeserialize
public class CustomGrantedAuthority implements GrantedAuthority {
    /*
    @Role.java of UserService is mapped with GrantedAuthority
     */
    private String authority;

    public CustomGrantedAuthority() {
    }

    public CustomGrantedAuthority(Role role) {
        this.authority = role.getValue();
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
