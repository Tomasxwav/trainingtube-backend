package com.traini.traini_backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class DepartmentDTO {
    
    private Long id;
    
    @NotEmpty(message = "El nombre del departamento no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre del departamento debe tener entre 2 y 50 caracteres")
    private String name;
    
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String description;
    
    private boolean active = true;
    
    // Constructores
    public DepartmentDTO() {}
    
    public DepartmentDTO(String name, String description) {
        this.name = name;
        this.description = description;
        this.active = true;
    }
    
    public DepartmentDTO(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}
