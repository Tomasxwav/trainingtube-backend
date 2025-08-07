package com.traini.traini_backend.dto.metrics.employee;

import java.util.Date;

public class EmployeeActivityDto {
    private Long employeeId;
    private String employeeName;
    private Date day;
    private Integer videosCompleted;

    public EmployeeActivityDto() {
    }

    public EmployeeActivityDto(Long employeeId, String employeeName, Date day) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.day = day;
        this.videosCompleted = 0;
    }

    public EmployeeActivityDto(Long employeeId, String employeeName, Date day, Integer videosCompleted) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.day = day;
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

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Integer getVideosCompleted() {
        return videosCompleted;
    }

    public void setVideosCompleted(Integer videosCompleted) {
        this.videosCompleted = videosCompleted;
    }
}
