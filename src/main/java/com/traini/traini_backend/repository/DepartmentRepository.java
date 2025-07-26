package com.traini.traini_backend.repository;

import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.models.CompanyModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<DepartmentModel, Long> {
    
    Optional<DepartmentModel> findByName(String name);
    
    List<DepartmentModel> findByActiveTrue();
    
    List<DepartmentModel> findByActiveFalse();
    
    boolean existsByName(String name);
    
    List<DepartmentModel> findAllByOrderByNameAsc();
    
    // Multi-tenant methods
    List<DepartmentModel> findByCompanyAndActiveTrue(CompanyModel company);
    
    List<DepartmentModel> findByCompanyAndActiveFalse(CompanyModel company);
    
    List<DepartmentModel> findByCompanyOrderByNameAsc(CompanyModel company);
    
    Optional<DepartmentModel> findByNameAndCompany(String name, CompanyModel company);
    
    boolean existsByNameAndCompany(String name, CompanyModel company);
    
    @Query("SELECT d FROM DepartmentModel d WHERE d.company.id = :companyId ORDER BY d.name ASC")
    List<DepartmentModel> findByCompanyIdOrderByNameAsc(@Param("companyId") Long companyId);
    
    @Query("SELECT d FROM DepartmentModel d WHERE d.company.id = :companyId AND d.active = true")
    List<DepartmentModel> findByCompanyIdAndActiveTrue(@Param("companyId") Long companyId);
}
