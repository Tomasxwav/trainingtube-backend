package com.traini.traini_backend.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class HibernateConfig {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public SessionFactory getSessionFactory() {
        return entityManagerFactory.unwrap(SessionFactory.class);
    }
}
