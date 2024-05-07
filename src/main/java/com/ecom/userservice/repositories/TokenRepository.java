package com.ecom.userservice.repositories;

import com.ecom.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValue(String tokenValue);

    //Select * From tokens Where value != and is_deleted= false;
    Optional<Token> findByValueAndDeleted(String token, Boolean isDeleted);
}
