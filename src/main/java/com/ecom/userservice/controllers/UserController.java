package com.ecom.userservice.controllers;

import com.ecom.userservice.dtos.*;
import com.ecom.userservice.exceptions.UserDetailsException;
import com.ecom.userservice.models.Token;
import com.ecom.userservice.models.User;
import com.ecom.userservice.repositories.TokenRepository;
import com.ecom.userservice.repositories.UserRepository;
import com.ecom.userservice.services.UserService;
import com.ecom.userservice.utilities.DtoConverter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService,
                          UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder, TokenRepository tokenRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignupRequestDto request) {
        User user = DtoConverter.fromSignUpUserDto(request);
        User newUser = null;
        try {
            newUser = userService.signUp(user);
        } catch (UserDetailsException e) {
            int httpSattusCode = 400;
            if (e.getMessage() != null) {
                if (e.getMessage().equals("User Exists")) {
                    httpSattusCode = 409;
                } else if (e.getMessage().equals("User Email not passed")) {
                    httpSattusCode = 406;
                } else if (e.getMessage().equals("User Password not passed")) {
                    httpSattusCode = 406;
                } else if (e.getMessage().equals("Username not passed")) {
                    httpSattusCode = 406;
                }
                UserDto userDto = new UserDto();
//                userDto.setMessage(e.getMessage());

                return new ResponseEntity<>(userDto, HttpStatusCode.valueOf(httpSattusCode));
            }
        }
        if (newUser != null) {
            UserDto userDto = DtoConverter.fromUser(newUser);
            return new ResponseEntity<>(userDto, HttpStatusCode.valueOf(201));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        Optional<User> dbUserOptional = userRepository.findByEmail(loginRequestDto.email());

        if (dbUserOptional.isEmpty()) {
            return null;
        }
        User userDb = dbUserOptional.get();
        if (!bCryptPasswordEncoder.matches(userDb.getPassword(), loginRequestDto.password())) {
            Token token = generateTokenSelf(userDb);
            UserDto userDto = new UserDto();
            userDto.setEmail(userDb.getEmail());
            userDto.setName(userDb.getName());
            TokenResponseDto tokenResponseDto = new TokenResponseDto(token.getValue(), token.getExpiryAt(), userDto);
            return new ResponseEntity<>(tokenResponseDto, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    /**
     * This is a method to implement token generation without using any library.
     *
     * @param user
     * @return
     */
    private Token generateTokenSelf(User user) {
        LocalDate currentTime = LocalDate.now();
        LocalDate thirtyDaysFromCurrentTime = currentTime.plusDays(30);

        Date expDate = Date.from(thirtyDaysFromCurrentTime.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Token token = new Token();
        token.setExpiryAt(expDate);
        //Token value is a randomly generated 128 bits characters, we can use apache commons lang3
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);
        tokenRepository.save(token);
        return token;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        userService.logout(logoutRequestDto.token());
        return null;
    }
}
