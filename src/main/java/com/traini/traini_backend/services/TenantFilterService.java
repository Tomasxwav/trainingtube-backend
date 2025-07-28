package com.traini.traini_backend.services;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.traini.traini_backend.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TenantFilterService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JwtUtil jwtUtil;

    public void enableTenantFilter(HttpServletRequest request, Long companyId) {
        String jwt = extractJwtFromRequest(request);
        if (jwt != null) {
            if (jwtUtil.isSuperAdmin(jwt)) {
                return; // No habilitar filtro para superadmin
            }
            
            if (companyId != null) {
                try {
                    Session session = entityManager.unwrap(Session.class);
                    org.hibernate.Filter filter = session.enableFilter("tenantFilter");
                    filter.setParameter("companyId", companyId);
                } catch (Exception e) {
                    // Silenciar errores de filtro
                }
            }
        }
    }

    public void disableTenantFilter() {
        try {
            Session session = entityManager.unwrap(Session.class);
            session.disableFilter("tenantFilter");
        } catch (Exception e) {
            // Silenciar errores al deshabilitar
        }
    }

    public void clearTenantFilter() {
        try {
            Session session = entityManager.unwrap(Session.class);
            if (session.getEnabledFilter("tenantFilter") != null) {
                session.disableFilter("tenantFilter");
            }
        } catch (Exception e) {
            // Ignorar errores al limpiar
        }
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

}