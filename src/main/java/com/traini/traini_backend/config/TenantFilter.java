package com.traini.traini_backend.config;

import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.repository.EmployeeRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class TenantFilter extends OncePerRequestFilter {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.isAuthenticated() && 
                !authentication.getPrincipal().equals("anonymousUser")) {
                
                String email = authentication.getName();
                Optional<EmployeeModel> employee = employeeRepository.findByEmail(email);
                
                if (employee.isPresent() && employee.get().getCompany() != null) {
                    TenantContext.setCurrentTenant(employee.get().getCompany().getId());
                }
            }
            
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
