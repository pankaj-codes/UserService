package com.ecom.userservice.services;

import com.ecom.userservice.configurations.KafkaProducerClient;
import com.ecom.userservice.dtos.SendEmailDto;
import com.ecom.userservice.exceptions.UserDetailsException;
import com.ecom.userservice.models.Token;
import com.ecom.userservice.models.User;
import com.ecom.userservice.repositories.TokenRepository;
import com.ecom.userservice.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class UserServiceImpl implements UserService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaProducerClient kafkaProducerClient;
    private final ObjectMapper objectMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           TokenRepository tokenRepository, KafkaProducerClient kafkaProducerClient, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.kafkaProducerClient = kafkaProducerClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public User signUp(User user) throws UserDetailsException {
        Optional<User> existingUserOptional = userRepository.findByEmail(user.getEmail());

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new UserDetailsException("User Email not passed");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserDetailsException("User Password not passed");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            throw new UserDetailsException("Username not passed");
        }

        if (existingUserOptional.isPresent()) {
            throw new UserDetailsException("User Exists");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User createdUser = userRepository.save(user);
        hashedPassword = null;

        // Once the signup is done, send the message to kafka for sending an email to the user.

        SendEmailDto sendEmailDto = new SendEmailDto();
        sendEmailDto.setTo(user.getEmail());
        sendEmailDto.setFrom("admin@ecom.com");
        sendEmailDto.setSubject("Welcome to ecom");
        sendEmailDto.setBody("Thanks for the registeration.");
        try {
            kafkaProducerClient.sendMessage("sendEmail", objectMapper.writeValueAsString(sendEmailDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return createdUser;
    }

    @Override
    public Token login(String email, String password) {
        return null;
    }

    @Override
    public Boolean logout(String token) {
        Optional<Token> optionalToken = tokenRepository.findByValue(token);
        if (optionalToken.isEmpty()) {
            return null;
        }
        Token dbToken = optionalToken.get();
//        Optional<Token> optionalToken = tokenRepository.findByValueAndIsDeleted(token, false);
        return null;
    }
}
