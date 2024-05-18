package com.ecom.userservice.security.models;

import com.ecom.userservice.models.Role;
import com.ecom.userservice.models.User;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@JsonDeserialize
public class CustomUserDetails implements UserDetails {

    private Long userId;
    private String password;
    private String username;
    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired;
    private Collection<CustomGrantedAuthority> authorities;
    private Boolean accountNonLocked;
    private Boolean enabled;

    public CustomUserDetails() {
    }

    public CustomUserDetails(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.accountNonLocked = true;
        this.enabled = true;

        //User object has list of roles
        this.authorities = new ArrayList<CustomGrantedAuthority>();

        for (Role role : user.getRoles()) {
            authorities.add(new CustomGrantedAuthority(role));
        }

        this.userId = user.getId();

    }

    @Override
    public Collection<CustomGrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getUserId() {
        return userId;
    }
}
