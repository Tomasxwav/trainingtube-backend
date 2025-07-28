package com.traini.traini_backend.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.repository.EmployeeRepository;
import com.traini.traini_backend.services.TenantFilterService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class TenantFilter extends OncePerRequestFilter {

    @Autowired
    private TenantFilterService tenantFilterService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        boolean filterEnabled = false;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {

                String email = authentication.getName();
                Optional<EmployeeModel> employee = employeeRepository.findByEmail(email);

                if (employee.isPresent() && employee.get().getCompany() != null) {
                    Long companyId = employee.get().getCompany().getId();
                    TenantContext.setCurrentTenant(companyId);
                    tenantFilterService.enableTenantFilter(request, companyId);
                    filterEnabled = true;
                }
            }

            filterChain.doFilter(request, response);
            
        } catch (Exception e) {
            // Silenciar errores del filtro
        } finally {
            TenantContext.clear();
            if (filterEnabled) {
                tenantFilterService.disableTenantFilter();
            }
        }
    }
}