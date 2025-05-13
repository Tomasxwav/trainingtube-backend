package com.traini.traini_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.repository.EmployeeRepository;
import com.traini.traini_backend.services.interfaces.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeModel> findAll() {
        return (List<EmployeeModel>) employeeRepository.findAll();
    }


    @Override
    public EmployeeModel findById(Long id) {
        return employeeRepository.findById(id).orElseThrow( () -> new IllegalArgumentException(String.format("The employee with id %s not found.", id)) );
    }

    @Override
    public EmployeeModel save(EmployeeModel employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public EmployeeModel update(Long id, EmployeeModel employee) {
        EmployeeModel employeeFound = findById(id);

        if( employee.getName() != null ) employeeFound.setName(employee.getName());
        if( employee.getEmail() != null ) employeeFound.setEmail(employee.getEmail());
        if( employee.getPassword() != null ) employeeFound.setPassword(employee.getPassword());
        if( employee.getRole() != null ) employeeFound.setRole(employee.getRole());

        return employeeRepository.save(employeeFound);
    }

    @Override
    public EmployeeModel delete(Long id) {
        EmployeeModel employeeFound = findById(id);
        employeeRepository.deleteById(id);
        return employeeFound;
    }

    

}
