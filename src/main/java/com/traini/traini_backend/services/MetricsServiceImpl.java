package com.traini.traini_backend.services;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.traini.traini_backend.dto.metrics.admin.AdminMetricsDto;
import com.traini.traini_backend.dto.metrics.employee.EmployeeMetricsDto;
import com.traini.traini_backend.dto.metrics.supervisor.SupervisorMetricsDto;
import com.traini.traini_backend.dto.metrics.supervisor.SupervisorProgressDto;
import com.traini.traini_backend.services.interfaces.MetricsService;

public class MetricsServiceImpl implements MetricsService {
    
    @Override
    public List<EmployeeMetricsDto> getEmployeeMetrics(Authentication authentication) {
        return null;
    }

    @Override
    public List<SupervisorMetricsDto> getAllSupervisorMetrics(Authentication authentication) {
        return null;
    }

    @Override
    public List<AdminMetricsDto> getAllAdminMetrics(Authentication authentication) {
        return null;
    }

    @Override
    public List<SupervisorProgressDto> getAllSupervisorProgress(Authentication authentication) {
        return null;
    }
}
