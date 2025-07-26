package com.traini.traini_backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "departments")
public class DepartmentModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    @NotEmpty
    @Size(min = 2, max = 50)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyModel company;
    
    // Relación con empleados
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<EmployeeModel> employees;
    
    // Relación con videos
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<VideoModel> videos;
    
    public DepartmentModel() {}
    
    public DepartmentModel(String name, String description) {
        this.name = name;
        this.description = description;
        this.active = true;
    }
    
    public DepartmentModel(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }
    
    public DepartmentModel(String name, String description, CompanyModel company) {
        this.name = name;
        this.description = description;
        this.company = company;
        this.active = true;
    }
    
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
    
    public Set<EmployeeModel> getEmployees() {
        return employees;
    }
    
    public void setEmployees(Set<EmployeeModel> employees) {
        this.employees = employees;
    }
    
    public Set<VideoModel> getVideos() {
        return videos;
    }
    
    public void setVideos(Set<VideoModel> videos) {
        this.videos = videos;
    }
    
    public CompanyModel getCompany() {
        return company;
    }
    
    public void setCompany(CompanyModel company) {
        this.company = company;
    }
}
