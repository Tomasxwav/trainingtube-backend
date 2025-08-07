package com.traini.traini_backend.services.interfaces;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.traini.traini_backend.dto.metrics.admin.AdminMetricsDto;
import com.traini.traini_backend.dto.metrics.employee.EmployeeActivityDto;
import com.traini.traini_backend.dto.metrics.employee.EmployeeMetricsDto;
import com.traini.traini_backend.dto.metrics.supervisor.SupervisorMetricsDto;
import com.traini.traini_backend.dto.metrics.supervisor.SupervisorProgressDto;

public interface MetricsService {
    EmployeeMetricsDto getEmployeeMetrics(Authentication authentication);

    List<EmployeeActivityDto> getEmployeeActivity(Authentication authentication);

    SupervisorMetricsDto getSupervisorMetrics(Authentication authentication);

    List<AdminMetricsDto> getAllAdminMetrics(Authentication authentication);

    List<SupervisorProgressDto> getAllSupervisorProgress(Authentication authentication);
}
