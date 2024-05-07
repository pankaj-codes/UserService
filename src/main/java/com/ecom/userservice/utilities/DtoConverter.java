package com.ecom.userservice.utilities;

import com.ecom.userservice.dtos.SignupRequestDto;
import com.ecom.userservice.dtos.UserDto;
import com.ecom.userservice.models.User;

public class DtoConverter {
    public static User fromSignUpUserDto(SignupRequestDto signupRequestDto) {
        User user = new User();
        if (signupRequestDto != null) {
            if (signupRequestDto.name() != null) {
                user.setName(signupRequestDto.name());
            }
            if (signupRequestDto.password() != null) {
                user.setPassword(signupRequestDto.password());
            }
            if (signupRequestDto.email() != null) {
                user.setEmail(signupRequestDto.email());
            }
        }
        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        if (user != null) {
            if (user.getName() != null) {
                userDto.setName(user.getName());
            }
            if (user.getEmail() != null) {
                userDto.setEmail(user.getEmail());
            }
        }
        return userDto;
    }
}
