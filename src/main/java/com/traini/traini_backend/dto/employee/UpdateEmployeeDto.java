package com.traini.traini_backend.dto.employee;

import com.traini.traini_backend.enums.Role;

public class UpdateEmployeeDto {
    private String name;
    private String email;
    private String password;
    private String role; 
    private Long departmentId;

    public UpdateEmployeeDto() {
    }

    public UpdateEmployeeDto(String name, String email, String password, String role, Long departmentId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Role getRoleEnum() {
        if (role == null) {
            return null;
        }
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }
}
