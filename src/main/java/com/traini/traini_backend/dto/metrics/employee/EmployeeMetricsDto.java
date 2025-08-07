package com.traini.traini_backend.dto.metrics.employee;

public class EmployeeMetricsDto {
    
    private final Long employeeId;
    private final String employeeName;
    private final Long totalVideos;
    private final Long pendingVideos;
    private final Long totalComments;
    private final Long totalInteractions;
    private final Long totalFavorites;

    public EmployeeMetricsDto(Long employeeId, String employeeName, Long totalVideos, 
                             Long pendingVideos, Long totalComments, Long totalInteractions, Long totalFavorites) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.totalVideos = totalVideos;
        this.pendingVideos = pendingVideos;
        this.totalComments = totalComments;
        this.totalInteractions = totalInteractions;
        this.totalFavorites = totalFavorites;
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

    public Long getTotalFavorites() {
        return totalFavorites;
    }
}
