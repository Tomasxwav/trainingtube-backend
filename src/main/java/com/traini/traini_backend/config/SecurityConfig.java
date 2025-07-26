package com.traini.traini_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.traini.traini_backend.security.JwtEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthentificationFilter jwtTokenFilter(){
        return new JwtAuthentificationFilter();
    }
    
    @Bean
    public TenantFilter tenantFilter(){
        return new TenantFilter();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // .cors(Customizer.withDefaults()) // Deshabilitado - usando CorsConfig en su lugar
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/auth/login", 
                "/auth/refresh-token")
                .permitAll()

                // Permisos exclusivos para Super Admin
                .requestMatchers("/companies/**")
                .hasRole("SUPER_ADMIN")

                // Permisos para Administrador
                .requestMatchers(
                "/employees/**",
                "/supervisors/**",
                "/videos/admin",
                "/metrics/**"
                ).hasAnyRole("SUPER_ADMIN", "ADMIN")
                
                // Permisos solo para Supervisor
                .requestMatchers(
                "/employees/department",
                "/metrics/department"
                ).hasAnyRole("SUPER_ADMIN", "SUPERVISOR")
                
                // Permisos para Supervisor y Empleado
                .requestMatchers(
                "/metrics/info/**"
                ).hasAnyRole("SUPER_ADMIN", "SUPERVISOR", "EMPLOYEE")

                // Permisos para Supervisor y Administrador
                .requestMatchers(
                "/auth/register"
                ).hasAnyRole("SUPER_ADMIN", "ADMIN", "SUPERVISOR")

                // Permisos generales (todos los roles autenticados)
                .requestMatchers(
                "/interactions/favorites/**", 
                "/interactions/likes/**", 
                "/videos/department",
                "/interactions/pending/**",
                "/comments/**"
                ).authenticated()

                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint()))
            .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(tenantFilter(), JwtAuthentificationFilter.class);
        return http.build();
    }

    @Bean
    public JwtEntryPoint jwtEntryPoint() {
        return new JwtEntryPoint();
    }
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}