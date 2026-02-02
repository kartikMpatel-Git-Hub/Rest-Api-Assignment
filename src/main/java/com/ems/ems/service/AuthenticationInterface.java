package com.ems.ems.service;

import com.ems.ems.dto.request.user.UserCreateDto;
import com.ems.ems.dto.request.user.UserLoginDto;
import com.ems.ems.dto.response.TokenResponseDto;
import com.ems.ems.dto.response.user.UserResponseDto;

public interface AuthenticationInterface {
    UserResponseDto createUser(UserCreateDto newUser);
    TokenResponseDto login(UserLoginDto credential);
}
