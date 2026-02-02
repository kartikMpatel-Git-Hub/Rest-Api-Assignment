package com.ems.ems.controller;

import com.ems.ems.dto.request.user.UserCreateDto;
import com.ems.ems.dto.request.user.UserLoginDto;
import com.ems.ems.dto.response.TokenResponseDto;
import com.ems.ems.dto.response.user.UserResponseDto;
import com.ems.ems.service.AuthenticationInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationInterface authenticationInterface;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(UserCreateDto newUser){
        return new ResponseEntity<>(
                authenticationInterface.createUser(newUser),
                HttpStatus.CREATED
        );
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(UserLoginDto credential){
        return new ResponseEntity<>(
                authenticationInterface.login(credential),
                HttpStatus.OK
        );
    }
}
