package com.traini.traini_backend.dto.metrics.employee;

public class EmployeeMetricsDto {
    
    private final Long employeeId;
    private final String employeeName;
    private final Long totalVideos;
    private final Long pendingVideos;
    private final Long totalComments;
    private final Long totalInteractions;

    public EmployeeMetricsDto(Long employeeId, String employeeName, Long totalVideos, 
                             Long pendingVideos, Long totalComments, Long totalInteractions) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.totalVideos = totalVideos;
        this.pendingVideos = pendingVideos;
        this.totalComments = totalComments;
        this.totalInteractions = totalInteractions;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public Long getTotalVideos() {
        return totalVideos;
    }

    public Long getPendingVideos() {
        return pendingVideos;
    }

    public Long getTotalComments() {
        return totalComments;
    }

    public Long getTotalInteractions() {
        return totalInteractions;
    }
}
