package com.traini.traini_backend.services;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.traini.traini_backend.dto.auth.LoginResponse;
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

    public LoginResponse authenticate(String email, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,password);

        Authentication authResult = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        /* String jwt = jwtUtil.generateToken(authResult); */
        LoginResponse session = new LoginResponse(jwtUtil.generateToken(authResult), jwtUtil.generateRefreshToken(authResult));
        return session;
    }

    public void registerUser(RegisterRequest registerDto){
        if (employeeService.existsByEmail(registerDto.getEmail())){
            throw new IllegalArgumentException("El usuario ya existe");
        }

        System.out.println("Registering user: " + registerDto.getEmail());

        EmployeeModel user = new EmployeeModel(registerDto.getName(),registerDto.getEmail(),passwordEncoder.encode(registerDto.getPassword()), registerDto.getRole(), registerDto.getDepartment()); 
        employeeService.save(user);
    }

    public String refreshAccessToken(String refreshToken) {
    String username = jwtUtil.extractUsername(refreshToken);

    System.out.println("Refreshing access token with: " + refreshToken);
    System.out.println("Refreshing access token for user: " + username);

    if (!jwtUtil.validateToken(refreshToken, employeeService.loadUserByUsername(username))) {
        throw new IllegalArgumentException("Refresh token inválido o expirado");
    }

    // Importante: NO creamos un token con authorities si el original no los tenía.
    // Esto fuerza que los refresh tokens no den acceso directo por sí solos.
    var userDetails = employeeService.loadUserByUsername(username);
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    return jwtUtil.generateToken(auth);
    }
}