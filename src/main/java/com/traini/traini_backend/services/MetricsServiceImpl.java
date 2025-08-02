package com.traini.traini_backend.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.traini.traini_backend.dto.metrics.admin.AdminMetricsDto;
import com.traini.traini_backend.dto.metrics.employee.EmployeeMetricsDto;
import com.traini.traini_backend.dto.metrics.supervisor.SupervisorMetricsDto;
import com.traini.traini_backend.dto.metrics.supervisor.SupervisorProgressDto;
import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.repository.EmployeeRepository;
import com.traini.traini_backend.repository.VideoRepository;
import com.traini.traini_backend.repository.InteractionRepository;
import com.traini.traini_backend.repository.CommentsRepository;
import com.traini.traini_backend.services.interfaces.MetricsService;

@Service
public class MetricsServiceImpl implements MetricsService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private VideoRepository videoRepository;
    
    @Autowired
    private InteractionRepository interactionRepository;
    
    @Autowired
    private CommentsRepository commentsRepository;
    
    @Override
    public List<EmployeeMetricsDto> getEmployeeMetrics(Authentication authentication) {
        String email = authentication.getName();
        
        Optional<EmployeeModel> employeeOpt = employeeRepository.findByEmail(email);
        if (!employeeOpt.isPresent()) {
            throw new RuntimeException("Empleado no encontrado con email: " + email);
        }
        
        EmployeeModel employee = employeeOpt.get();
        DepartmentModel department = employee.getDepartment();
        
        Long totalVideos = videoRepository.countByDepartment(department);
        Long pendingVideos = (long) interactionRepository.findByEmployeeIdAndIsPending(employee.getId(), true).size();
        Long totalComments = (long) commentsRepository.findByEmployeeId(employee.getId()).size();
        Long totalInteractions = (long) interactionRepository.findByEmployeeId(employee.getId()).size();
        
        EmployeeMetricsDto metricsDto = new EmployeeMetricsDto(
            employee.getId(),
            employee.getName(),
            totalVideos,
            pendingVideos,
            totalComments,
            totalInteractions
        );
        
        return Arrays.asList(metricsDto);
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
