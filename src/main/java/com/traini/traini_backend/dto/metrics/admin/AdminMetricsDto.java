package com.traini.traini_backend.dto.metrics.admin;

public class AdminMetricsDto {
    private Long departmentId;
    private String departmentName;
    private Long totalEmployees;
    private Long totalVideos;
    private Long totalInteractions;
    private Long totalComments;
    private Double averageCompletionRate;
    private Long totalFavorites;

    public AdminMetricsDto() {
    }

    public AdminMetricsDto(Long departmentId, String departmentName, Long totalEmployees, Long totalVideos, 
                           Long totalInteractions, Long totalComments, Double averageCompletionRate, 
                           Long totalFavorites) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.totalEmployees = totalEmployees;
        this.totalVideos = totalVideos;
        this.totalInteractions = totalInteractions;
        this.totalComments = totalComments;
        this.averageCompletionRate = averageCompletionRate;
        this.totalFavorites = totalFavorites;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public Long getTotalEmployees() {
        return totalEmployees;
    }

    public Long getTotalVideos() {
        return totalVideos;
    }

    public Long getTotalInteractions() {
        return totalInteractions;
    }

    public Long getTotalComments() {
        return totalComments;
    }

    public Double getAverageCompletionRate() {
        return averageCompletionRate;
    }

    public Long getTotalFavorites() {
        return totalFavorites;
    }
    
}
