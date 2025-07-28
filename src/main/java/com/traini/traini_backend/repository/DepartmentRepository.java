package com.traini.traini_backend.repository;

import com.traini.traini_backend.models.CompanyModel;
import com.traini.traini_backend.models.DepartmentModel;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<DepartmentModel, Long> {
    
    Optional<DepartmentModel> findByName(String name);
    
    List<DepartmentModel> findByActiveTrue();
    
    List<DepartmentModel> findByActiveFalse();
    
    boolean existsByName(String name);

    boolean existsByNameAndCompany(String name, CompanyModel company);
    
    List<DepartmentModel> findAllByOrderByNameAsc();
}
