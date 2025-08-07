package com.traini.traini_backend.dto.metrics.supervisor;

public class SupervisorProgressDto {
      private Long employeeId;
    private String employeeName;
    private Integer videosCompleted;

    public SupervisorProgressDto() {
    }

    public SupervisorProgressDto(Long employeeId, String employeeName) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.videosCompleted = 0;
    }

    public SupervisorProgressDto(Long employeeId, String employeeName, Integer videosCompleted) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.videosCompleted = videosCompleted;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getVideosCompleted() {
        return videosCompleted;
    }

    public void setVideosCompleted(Integer videosCompleted) {
        this.videosCompleted = videosCompleted;
    }
}
