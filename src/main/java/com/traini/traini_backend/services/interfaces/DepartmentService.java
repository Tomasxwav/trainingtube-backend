package com.traini.traini_backend.services.interfaces;

import com.traini.traini_backend.models.DepartmentModel;
import java.util.List;

public interface DepartmentService {
    
    List<DepartmentModel> findAll();
    
    List<DepartmentModel> findAllActive();
    
    List<DepartmentModel> findAllInactive();
    
    DepartmentModel findById(Long id);
    
    DepartmentModel findByName(String name);
    
    DepartmentModel save(DepartmentModel department);
    
    DepartmentModel update(Long id, DepartmentModel department);
    
    void delete(Long id);
    
    void toggleActiveStatus(Long id);
    
    boolean existsByName(String name);
}
