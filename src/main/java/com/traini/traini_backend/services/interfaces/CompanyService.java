package com.traini.traini_backend.services.interfaces;

import java.util.List;
import com.traini.traini_backend.models.CompanyModel;

public interface CompanyService {
    List<CompanyModel> findAll();
    List<CompanyModel> findAllActive();
    List<CompanyModel> findAllInactive();
    CompanyModel findById(Long id);
    CompanyModel findByName(String companyName);
    CompanyModel save(CompanyModel company);
    CompanyModel update(Long id, CompanyModel companyDetails);
    void delete(Long id);
    void toggleActiveStatus(Long id);
    boolean existsByName(String companyName);
}
