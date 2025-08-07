package com.traini.traini_backend.dto.metrics.supervisor;

public class SupervisorMetricsDto {
    private Long departmentId;
    private String departmentName;
    private Long totalEmployees;
    private Long totalVideos;
    private Long totalInteractions;
    private Long totalComments;
    private Double averageCompletionRate;
    private Long totalFavorites;

    public SupervisorMetricsDto() {
    }

    public SupervisorMetricsDto(Long departmentId, String departmentName, Long totalEmployees, Long totalVideos, 
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

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(Long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public Long getTotalVideos() {
        return totalVideos;
    }

    public void setTotalVideos(Long totalVideos) {
        this.totalVideos = totalVideos;
    }

    public Long getTotalInteractions() {
        return totalInteractions;
    }

    public void setTotalInteractions(Long totalInteractions) {
        this.totalInteractions = totalInteractions;
    }

    public Long getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Long totalComments) {
        this.totalComments = totalComments;
    }

    public Double getAverageCompletionRate() {
        return averageCompletionRate;
    }

    public void setAverageCompletionRate(Double averageCompletionRate) {
        this.averageCompletionRate = averageCompletionRate;
    }

    public Long getTotalFavorites() {
        return totalFavorites;
    }

    public void setTotalFavorites(Long totalFavorites) {
        this.totalFavorites = totalFavorites;
    }
}
