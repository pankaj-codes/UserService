package com.ecom.userservice.dtos;

import java.util.Date;

public record TokenResponseDto(String token, Date expiryAt, UserDto userDto) {
}
