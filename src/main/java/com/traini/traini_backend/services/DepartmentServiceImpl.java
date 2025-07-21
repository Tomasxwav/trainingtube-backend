package com.traini.traini_backend.services;

import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.repository.DepartmentRepository;
import com.traini.traini_backend.services.interfaces.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentModel> findAll() {
        return (List<DepartmentModel>) departmentRepository.findAllByOrderByNameAsc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentModel> findAllActive() {
        return departmentRepository.findByActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentModel> findAllInactive() {
        return departmentRepository.findByActiveFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public DepartmentModel findById(Long id) {
        Optional<DepartmentModel> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            return department.get();
        }
        throw new RuntimeException("Department not found with id: " + id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DepartmentModel findByName(String name) {
        Optional<DepartmentModel> department = departmentRepository.findByName(name);
        if (department.isPresent()) {
            return department.get();
        }
        throw new RuntimeException("Department not found with name: " + name);
    }
    
    @Override
    public DepartmentModel save(DepartmentModel department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new RuntimeException("Department with name '" + department.getName() + "' already exists");
        }
        return departmentRepository.save(department);
    }
    
    @Override
    public DepartmentModel update(Long id, DepartmentModel departmentDetails) {
        DepartmentModel department = findById(id);
        
        // Verificar si el nuevo nombre ya existe en otro departamento
        if (!department.getName().equals(departmentDetails.getName()) && 
            departmentRepository.existsByName(departmentDetails.getName())) {
            throw new RuntimeException("Department with name '" + departmentDetails.getName() + "' already exists");
        }
        
        department.setName(departmentDetails.getName());
        department.setDescription(departmentDetails.getDescription());
        department.setActive(departmentDetails.isActive());
        
        return departmentRepository.save(department);
    }
    
    @Override
    public void delete(Long id) {
        DepartmentModel department = findById(id);
        
        // Verificar si el departamento tiene empleados o videos asociados
        if (department.getEmployees() != null && !department.getEmployees().isEmpty()) {
            throw new RuntimeException("Cannot delete department with associated employees");
        }
        
        if (department.getVideos() != null && !department.getVideos().isEmpty()) {
            throw new RuntimeException("Cannot delete department with associated videos");
        }
        
        departmentRepository.delete(department);
    }
    
    @Override
    public void toggleActiveStatus(Long id) {
        DepartmentModel department = findById(id);
        department.setActive(!department.isActive());
        departmentRepository.save(department);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }
}
