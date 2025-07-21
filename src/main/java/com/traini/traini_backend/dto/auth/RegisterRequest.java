package com.traini.traini_backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.traini.traini_backend.enums.Role;

public class RegisterRequest {


    private String name;
    private String email;
    private String password;
    private Role role;
    @JsonProperty("department_id")
    private Long departmentId;

    
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}