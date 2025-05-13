package com.traini.traini_backend.services.interfaces;

import java.util.List;

import com.traini.traini_backend.models.EmployeeModel;

public interface EmployeeService {
    List<EmployeeModel> findAll();
    EmployeeModel findById(Long id);
    EmployeeModel save(EmployeeModel employee);
    EmployeeModel update(Long id, EmployeeModel employee);
    EmployeeModel delete(Long id);
}
