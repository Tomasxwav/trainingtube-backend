package com.traini.traini_backend.dto.video;

public class UpdateVideoDto {
    private String title;
    private String description;
    private Long departmentId;

    public UpdateVideoDto() {
    }

    public UpdateVideoDto(String title, String description, Long departmentId) {
        this.title = title;
        this.description = description;
        this.departmentId = departmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
