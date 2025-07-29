package com.traini.traini_backend.services;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.traini.traini_backend.dto.auth.LoginResponse;
import com.traini.traini_backend.dto.auth.RegisterRequest;
import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.models.RoleModel;
import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.models.CompanyModel;
import com.traini.traini_backend.repository.RoleRepository;
import com.traini.traini_backend.repository.CompanyRepository;
import com.traini.traini_backend.services.interfaces.DepartmentService;
import com.traini.traini_backend.security.JwtUtil;
import com.traini.traini_backend.config.TenantContext;

@Service
public class AuthService {

    private final EmployeeServiceImpl employeeService;
    private final DepartmentService departmentService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;

    public AuthService(EmployeeServiceImpl employeeService, DepartmentService departmentService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManagerBuilder authenticationManagerBuilder, RoleRepository roleRepository, CompanyRepository companyRepository) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
    }

    public LoginResponse authenticate(String email, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,password);

        Authentication authResult = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authResult);

        List<String> authorities = authResult.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        /* String jwt = jwtUtil.generateToken(authResult); */
        LoginResponse session = new LoginResponse(jwtUtil.generateToken(authResult), jwtUtil.generateRefreshToken(authResult), authorities);
        return session;
    }

    public void registerUser(RegisterRequest registerDto){
        if (employeeService.existsByEmail(registerDto.getEmail())){
            throw new IllegalArgumentException("El usuario ya existe");
        }

        RoleModel roleModel = roleRepository.findByName(registerDto.getRole())
            .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        DepartmentModel departmentModel;
        try {
            departmentModel = departmentService.findById(registerDto.getDepartmentId());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Departamento no encontrado con ID: " + registerDto.getDepartmentId());
        }

        EmployeeModel user;
        Long tenantId = TenantContext.getCurrentTenant();
        CompanyModel company = null;
        
        if (tenantId == null) {
            if (registerDto.getCompanyId() == null) {
                throw new IllegalArgumentException("Company ID es requerido para registrar usuarios");
            }
            company = companyRepository.findById(registerDto.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("Company no encontrada con ID: " + registerDto.getCompanyId()));
        } else {
            if (registerDto.getCompanyId() != null && !registerDto.getCompanyId().equals(tenantId)) {
                throw new IllegalArgumentException("No puede crear usuarios en una company diferente a la suya");
            }
           
            
            company = companyRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

            if (departmentModel.getCompany() != company) {
                throw new IllegalArgumentException("No existe un departamento con ese ID en la company seleccionada");
            }
        }

        user = new EmployeeModel(
            registerDto.getName(),
            registerDto.getEmail(),
            passwordEncoder.encode(registerDto.getPassword()),
            roleModel,
            departmentModel
        );
        
        if (company != null) {
            user.setCompany(company);
        }

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


    public LoginResponse refreshAccessTokenWithAuthorities(String refreshToken) {
        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = employeeService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(refreshToken, userDetails)) {
            throw new IllegalArgumentException("Refresh token inválido o expirado");
        }

        String newAccessToken = jwtUtil.generateToken(
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        List<String> authorities = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        return new LoginResponse(newAccessToken, refreshToken, authorities);
    }
}