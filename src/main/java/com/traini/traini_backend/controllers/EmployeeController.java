package com.traini.traini_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.services.interfaces.EmployeeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<?> getEmployees() {
        List<EmployeeModel> employees = employeeService.findAll();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@RequestParam String id) {
        EmployeeModel employee = employeeService.findById(Long.parseLong(id));
        return ResponseEntity.ok(employee);
    }


    @PostMapping
    public ResponseEntity<?> postEmployee(@RequestBody EmployeeModel employee) {
        EmployeeModel employeeCreated = employeeService.save(employee);
        return ResponseEntity.ok(employeeCreated);
    }


    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        try {
            String email = authentication.getName();
            return ResponseEntity.ok(employeeService.getUserByEmail(email));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
        

}
