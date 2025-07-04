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
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/auth/register", 
                "/auth/login", 
                "/auth/refresh-token")
                .permitAll()

                // Permisos para Administrador
                .requestMatchers(
                "/employees/**",
                "/supervisors/**",
                "/employees/**",
                "/admin/videos/**",
                "/metrics/**"
                ).hasRole("ADMIN")
                
                // Permisos para Supervisor
                .requestMatchers(
                    "/department/employees/**",
                    "/department/videos/**",
                    "/department/metrics/**"
                ).hasRole("SUPERVISOR")
                
                // Permisos para Empleado
                .requestMatchers(
                    "/employees/videos/**",
                    "/metrics/info/**"
                ).hasRole("EMPLOYEE")
                
                // Permisos generales (todos los roles autenticados)
                .requestMatchers(
                    "/interactions/favorites/**", 
                    "/interactions/likes/**", 
                    "/interactions/pending/**"
                ).authenticated()

                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint()))
            .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
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