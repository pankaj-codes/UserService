package com.ecom.userservice.services;

import com.ecom.userservice.exceptions.UserDetailsException;
import com.ecom.userservice.models.Token;
import com.ecom.userservice.models.User;

public interface UserService {

    public User signUp(User user) throws UserDetailsException;
    public Token login(String email, String password);
    public Boolean logout(String token);
}
