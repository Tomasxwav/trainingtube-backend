package com.traini.traini_backend.repository;

import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.models.VideoModel;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;

public interface VideoRepository extends CrudRepository<VideoModel, Long> {
    List<VideoModel> findByDepartment(DepartmentModel department);
    
    // Multi-tenant methods
    @Query("SELECT v FROM VideoModel v WHERE v.department.company.id = :companyId")
    List<VideoModel> findByCompanyId(@Param("companyId") Long companyId);
    
    @Query("SELECT v FROM VideoModel v WHERE v.department.company.id = :companyId AND v.department.id = :departmentId")
    List<VideoModel> findByCompanyIdAndDepartmentId(@Param("companyId") Long companyId, @Param("departmentId") Long departmentId);
}