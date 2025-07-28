package com.traini.traini_backend.repository;

import com.traini.traini_backend.models.CompanyModel;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends CrudRepository<CompanyModel, Long> {

    Optional<CompanyModel> findByCompanyName(String companyName);

    List<CompanyModel> findByActiveTrue();

    List<CompanyModel> findByActiveFalse();

    boolean existsByCompanyName(String companyName);

    List<CompanyModel> findAllByOrderByCompanyNameAsc();
}