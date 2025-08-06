package com.traini.traini_backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traini.traini_backend.dto.metrics.admin.AdminMetricsDto;
import com.traini.traini_backend.dto.metrics.employee.EmployeeActivityDto;
import com.traini.traini_backend.dto.metrics.employee.EmployeeMetricsDto;
import com.traini.traini_backend.dto.metrics.supervisor.SupervisorMetricsDto;
import com.traini.traini_backend.dto.metrics.supervisor.SupervisorProgressDto;
import com.traini.traini_backend.services.interfaces.MetricsService;

@RestController
@RequestMapping("/metrics")
public class MetricsController {
    
    @Autowired
    private MetricsService metricsService;
    
    @GetMapping("/employee")
    public ResponseEntity<EmployeeMetricsDto> getEmployeeMetrics(Authentication authentication) {
        try {
            EmployeeMetricsDto metrics = metricsService.getEmployeeMetrics(authentication);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/employee/activity")
    public ResponseEntity<List<EmployeeActivityDto>> getEmployeeActivity(Authentication authentication) {
        try {
            List<EmployeeActivityDto> activity = metricsService.getEmployeeActivity(authentication);
            return ResponseEntity.ok(activity);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/supervisor")
    public ResponseEntity<SupervisorMetricsDto> getSupervisorMetrics(Authentication authentication) {
        try {
            SupervisorMetricsDto metrics = metricsService.getSupervisorMetrics(authentication);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/supervisor/progress")
    public ResponseEntity<List<SupervisorProgressDto>> getSupervisorProgress(Authentication authentication) {
        try {
            List<SupervisorProgressDto> progress = metricsService.getAllSupervisorProgress(authentication);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/admin")
    public ResponseEntity<List<AdminMetricsDto>> getAllAdminMetrics(Authentication authentication) {
        try {
            List<AdminMetricsDto> metrics = metricsService.getAllAdminMetrics(authentication);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
