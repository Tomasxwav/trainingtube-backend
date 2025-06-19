package com.traini.traini_backend.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.enums.Department;
import java.util.List;


public interface EmployeeRepository extends CrudRepository<EmployeeModel, Long> {
    Optional<EmployeeModel> findByName(String name);
    Optional<EmployeeModel> findByEmail(String email);

    List<EmployeeModel> findByDepartment(Department department);

    boolean existsByEmail(String email);
}
