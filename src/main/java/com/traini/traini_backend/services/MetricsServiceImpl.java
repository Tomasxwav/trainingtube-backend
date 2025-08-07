package com.traini.traini_backend.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.traini.traini_backend.dto.metrics.admin.AdminMetricsDto;
import com.traini.traini_backend.dto.metrics.employee.EmployeeActivityDto;
import com.traini.traini_backend.dto.metrics.employee.EmployeeMetricsDto;
import com.traini.traini_backend.dto.metrics.supervisor.SupervisorMetricsDto;
import com.traini.traini_backend.dto.metrics.supervisor.SupervisorProgressDto;
import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.models.InteractionModel;
import com.traini.traini_backend.repository.EmployeeRepository;
import com.traini.traini_backend.repository.VideoRepository;
import com.traini.traini_backend.repository.InteractionRepository;
import com.traini.traini_backend.repository.CommentsRepository;
import com.traini.traini_backend.repository.DepartmentRepository;
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

    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Override
    public EmployeeMetricsDto getEmployeeMetrics(Authentication authentication) {
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
        Long totalFavorites = (long) interactionRepository.findByEmployeeIdAndIsFavorite(employee.getId(), true).size();
        
        EmployeeMetricsDto metricsDto = new EmployeeMetricsDto(
            employee.getId(),
            employee.getName(),
            totalVideos,
            pendingVideos,
            totalComments,
            totalInteractions,
            totalFavorites

        );
        return metricsDto;
    }

    @Override
    public List<EmployeeActivityDto> getEmployeeActivity(Authentication authentication) {
        String email = authentication.getName();
        
        Optional<EmployeeModel> employeeOpt = employeeRepository.findByEmail(email);
        if (!employeeOpt.isPresent()) {
            throw new RuntimeException("Empleado no encontrado con email: " + email);
        }

        EmployeeModel employee = employeeOpt.get();
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);
        
        Date startDateAsDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDateAsDate = Date.from(endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        List<InteractionModel> finalizedInteractions = interactionRepository
            .findByEmployeeIdAndFinalizedDateBetween(employee.getId(), startDateAsDate, endDateAsDate);
        
        Map<LocalDate, Long> videosByDate = finalizedInteractions.stream()
            .collect(Collectors.groupingBy(
                interaction -> interaction.getFinalizedDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate(),
                Collectors.counting()
            ));
        
        List<EmployeeActivityDto> activities = new java.util.ArrayList<>();
        for (int i = 0; i < 30; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            Date currentDateAsDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Long videosCompleted = videosByDate.getOrDefault(currentDate, 0L);
            
            activities.add(new EmployeeActivityDto(
                employee.getId(),
                employee.getName(),
                currentDateAsDate,
                videosCompleted.intValue()
            ));
        }

        return activities;
    }

    @Override
    public SupervisorMetricsDto getSupervisorMetrics(Authentication authentication) {
        String email = authentication.getName();
        
        Optional<EmployeeModel> employeeOpt = employeeRepository.findByEmail(email);
        if (!employeeOpt.isPresent()) {
            throw new RuntimeException("Empleado no encontrado con email: " + email);
        }

        DepartmentModel department = employeeOpt.get().getDepartment();
        Long departmentId = department.getId();
        String departmentName = department.getName();
        Long totalEmployees = employeeRepository.countByDepartmentId(departmentId);
        Long totalVideos = videoRepository.countByDepartment(department);
        Long totalInteractions = (long) interactionRepository.findByEmployeeId(employeeOpt.get().getId()).size();
        Long totalComments = (long) commentsRepository.findByEmployeeId(employeeOpt.get().getId()).size();
        // Double averageCompletionRate = interactionRepository.getAverageProgressByEmployee(employeeOpt.get().getId());
        //Long totalFavorites = videoRepository.countFavoritesByDepartmentId(departmentId);
        Double averageCompletionRate = 0.0;
        Long totalFavorites = (long) 0; 
        
        SupervisorMetricsDto metricsDto = new SupervisorMetricsDto(
            departmentId,
            departmentName,
            totalEmployees,
            totalVideos,
            totalInteractions,
            totalComments,
            averageCompletionRate,
            totalFavorites
        );
        
        return metricsDto;
    }

    @Override
    public List<SupervisorProgressDto> getAllSupervisorProgress(Authentication authentication) {
        String email = authentication.getName();

        Optional<EmployeeModel> employeeOpt = employeeRepository.findByEmail(email);
        if (!employeeOpt.isPresent()) {
            throw new RuntimeException("Empleado no encontrado con email: " + email);
        }

        DepartmentModel department = employeeOpt.get().getDepartment();
        
        List<EmployeeModel> employees = employeeRepository.findAllByDepartmentId(department.getId());
        List<SupervisorProgressDto> progress = new java.util.ArrayList<>();

        for (EmployeeModel emp : employees) {
            int completedVideos = interactionRepository.findByEmployeeIdAndIsPending(emp.getId(), false).size();

            progress.add(new SupervisorProgressDto(
                emp.getId(),
                emp.getName(),
                completedVideos
            ));
        }
        
        return progress;
        
    }


    @Override
    public List<AdminMetricsDto> getAllAdminMetrics(Authentication authentication) {
        String email = authentication.getName();

        Optional<EmployeeModel> employeeOpt = employeeRepository.findByEmail(email);
        if (!employeeOpt.isPresent()) {
            throw new RuntimeException("Empleado no encontrado con email: " + email);
        }

        List<DepartmentModel> departments = departmentRepository.findByActiveTrue();
        if (departments.isEmpty()) {
            throw new RuntimeException("No hay departamentos activos.");
        }

        List<AdminMetricsDto> metrics = new java.util.ArrayList<>();

        for (DepartmentModel department : departments) {
            int employeesCount = employeeRepository.findByDepartment(department).size();
            Long totalVideos = videoRepository.countByDepartment(department);
            
            List<EmployeeModel> departmentEmployees = employeeRepository.findByDepartment(department);
            Long totalInteractions = 0L;
            Long totalFavorites = 0L;
            Long totalFinalized = 0L;
            for (EmployeeModel emp : departmentEmployees) {
                totalInteractions += interactionRepository.findByEmployeeId(emp.getId()).size();
                totalFavorites += interactionRepository.findByEmployeeIdAndIsFavorite(emp.getId(), true).size();
                totalFinalized += interactionRepository.findByEmployeeIdAndIsPending(emp.getId(), false).size();
            }
            
            Long totalComments = 0L;
            for (EmployeeModel emp : departmentEmployees) {
                totalComments += commentsRepository.findByEmployeeId(emp.getId()).size();
            }

            Double averageCompletionRate = (totalFinalized == 0) ? 0.0 : totalVideos / (double) totalFinalized;

            AdminMetricsDto adminMetricsDto = new AdminMetricsDto(
                department.getId(),
                department.getName(),
                (long) employeesCount,
                totalVideos,
                totalInteractions,
                totalComments,
                averageCompletionRate, 
                totalFavorites,
                totalFinalized
            );
            
            metrics.add(adminMetricsDto);
        }

        return metrics;
    }
}
