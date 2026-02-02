package com.ems.ems.service.impl;

import com.ems.ems.dto.request.user.UserCreateDto;
import com.ems.ems.dto.request.user.UserLoginDto;
import com.ems.ems.dto.response.TokenResponseDto;
import com.ems.ems.dto.response.user.UserResponseDto;
import com.ems.ems.exception.RegisterException;
import com.ems.ems.model.UserModel;
import com.ems.ems.repository.UserRepository;
import com.ems.ems.security.JwtTokenHelper;
import com.ems.ems.service.AuthenticationInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthenticationService implements AuthenticationInterface {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;
    private final UserDetailsService userDetailsService;
    private final JwtTokenHelper jwtTokenHelper;


    @Override
    public UserResponseDto createUser(UserCreateDto newUser) {
        UserModel user = new UserModel();
        System.out.println(newUser.getUserName());
        user.setUserName(newUser.getUserName());
        System.out.println(user.getUsername());
        user.setHashPassword(passwordEncoder.encode(newUser.getPassword()));
        UserModel savedUser = userRepository.save(user);
        return mapper.map(savedUser,UserResponseDto.class);
    }

    @Override
    public TokenResponseDto login(UserLoginDto credential) {
        List<String> errors = validateLoginRequest(credential);
        if (!errors.isEmpty()) {
            throw new RegisterException(errors);
        }
        authenticate(credential.getUserName(), credential.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(credential.getUserName());
        String token = jwtTokenHelper.generateToken(userDetails);
        return new TokenResponseDto(token);
    }

    private List<String> validateLoginRequest(UserLoginDto request) {
        List<String> errors = new ArrayList<>();
        if (isNullOrEmpty(request.getUserName())) errors.add("Username Required");
        if (isNullOrEmpty(request.getPassword())) errors.add("Password Required");
        return errors;
    }

    private void authenticate(String userName, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userName, password);
        System.out.println(authToken);
        try {
            authenticationManager.authenticate(authToken);
        } catch (DisabledException e) {
            throw new DisabledException("User Is Disabled!");
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
