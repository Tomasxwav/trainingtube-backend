package com.traini.traini_backend.services;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.traini.traini_backend.dto.auth.RegisterRequest;
import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.security.JwtUtil;

@Service
public class AuthService {

    private final EmployeeServiceImpl employeeService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthService(EmployeeServiceImpl employeeService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    public String authenticate(String email, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,password);

        Authentication authResult = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        String jwt = jwtUtil.generateToken(authResult);
        return jwt;
    }

    public void registerUser(RegisterRequest registerDto){
        if (employeeService.existsByEmail(registerDto.getEmail())){
            throw new IllegalArgumentException("El usuario ya existe");
        }

        System.out.println("Registering user: " + registerDto.getEmail());

        EmployeeModel user = new EmployeeModel(registerDto.getName(),registerDto.getEmail(),passwordEncoder.encode(registerDto.getPassword()), registerDto.getRole()); 
        employeeService.save(user);
    }
}