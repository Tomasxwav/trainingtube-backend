package com.traini.traini_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.services.interfaces.EmployeeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




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

    @PutMapping("/{employeeId}")
    public ResponseEntity<?> putEmployee(@PathVariable String employeeId, @RequestBody EmployeeModel employee) {
        EmployeeModel updatedEmployee = employeeService.update(Long.parseLong(employeeId), employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String employeeId) {
        EmployeeModel deletedEmployee = employeeService.delete(Long.parseLong(employeeId));
        return ResponseEntity.ok(deletedEmployee);
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

    @GetMapping("/department")
    public ResponseEntity<?> getDepartmentEmployees(Authentication authentication) {
        try {
            return ResponseEntity.ok(employeeService.findByDepartment(authentication));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/department")
    public ResponseEntity<?> postDepartmentEmployee(@RequestBody EmployeeModel employee, Authentication authentication) {
        try {
            EmployeeModel employeeCreated = employeeService.saveAsSupervisor(employee, authentication);
            return ResponseEntity.ok(employeeCreated);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/department/{employeeId}")
    public ResponseEntity<?> putDepartmentEmployee(@PathVariable String employeeId, @RequestBody EmployeeModel employee, Authentication authentication) {
        try {
            EmployeeModel updatedEmployee = employeeService.updateAsSupervisor(Long.parseLong(employeeId), employee, authentication);
            return ResponseEntity.ok(updatedEmployee);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/department/{employeeId}")
    public ResponseEntity<?> deleteDepartmentEmployee(@PathVariable String employeeId, Authentication authentication) {
        try {
            EmployeeModel deletedEmployee = employeeService.deleteAsSupervisor(Long.parseLong(employeeId), authentication);
            return ResponseEntity.ok(deletedEmployee);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

}
