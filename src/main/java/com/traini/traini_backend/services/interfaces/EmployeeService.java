package com.traini.traini_backend.services.interfaces;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.traini.traini_backend.models.EmployeeModel;

public interface EmployeeService {
    List<EmployeeModel> findAll();
    EmployeeModel findById(Long id);
    EmployeeModel save(EmployeeModel employee);
    EmployeeModel saveAsSupervisor(EmployeeModel employee, Authentication authentication);
    EmployeeModel update(Long id, EmployeeModel employee);
    EmployeeModel updateAsSupervisor(Long id, EmployeeModel employee, Authentication authentication);
    EmployeeModel delete(Long id);
    EmployeeModel deleteAsSupervisor(Long id, Authentication authentication);
    boolean existsByEmail(String email);
    EmployeeModel getUserByEmail(String email);
    List<EmployeeModel> findByDepartment(Authentication authentication);
}
