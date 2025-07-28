package com.traini.traini_backend.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;

@Aspect
@Component
public class TenantFilterAspect {

    @Autowired
    private EntityManager entityManager;

    @Around("execution(* com.traini.traini_backend.repository..*.*(..)) && " +
            "!execution(* com.traini.traini_backend.repository.EmployeeRepository.findByEmail(..))")
    public Object enableTenantFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        Long tenantId = TenantContext.getCurrentTenant();
        
        if (tenantId != null) {
            try {
                Session session = entityManager.unwrap(Session.class);
                org.hibernate.Filter filter = session.enableFilter("tenantFilter");
                filter.setParameter("companyId", tenantId);
            } catch (Exception e) {
                // Silenciar errores del aspecto
            }
        }
        
        return joinPoint.proceed();
    }
}
