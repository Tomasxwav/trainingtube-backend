package com.traini.traini_backend.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.traini.traini_backend.models.EmployeeModel;

public interface EmployeeRepository extends CrudRepository<EmployeeModel, Long> {
    Optional<EmployeeModel> findByName(String name);
}
