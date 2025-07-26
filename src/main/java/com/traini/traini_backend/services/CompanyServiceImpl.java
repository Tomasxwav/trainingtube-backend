package com.traini.traini_backend.services;

import com.traini.traini_backend.models.CompanyModel;
import com.traini.traini_backend.repository.CompanyRepository;
import com.traini.traini_backend.services.interfaces.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<CompanyModel> findAll() {
        return (List<CompanyModel>) companyRepository.findAllByOrderByCompanyNameAsc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CompanyModel> findAllActive() {
        return companyRepository.findByActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CompanyModel> findAllInactive() {
        return companyRepository.findByActiveFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public CompanyModel findById(Long id) {
        Optional<CompanyModel> company = companyRepository.findById(id);
        if (company.isPresent()) {
            return company.get();
        }
        throw new RuntimeException("Company not found with id: " + id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CompanyModel findByName(String companyName) {
        Optional<CompanyModel> company = companyRepository.findByCompanyName(companyName);
        if (company.isPresent()) {
            return company.get();
        }
        throw new RuntimeException("Company not found with name: " + companyName);
    }
    
    @Override
    public CompanyModel save(CompanyModel company) {
        if (companyRepository.existsByCompanyName(company.getCompanyName())) {
            throw new RuntimeException("Company with name '" + company.getCompanyName() + "' already exists");
        }
        return companyRepository.save(company);
    }
    
    @Override
    public CompanyModel update(Long id, CompanyModel companyDetails) {
        CompanyModel company = findById(id);
        
        // Verificar si el nuevo nombre ya existe en otra compañía
        if (!company.getCompanyName().equals(companyDetails.getCompanyName()) && 
            companyRepository.existsByCompanyName(companyDetails.getCompanyName())) {
            throw new RuntimeException("Company with name '" + companyDetails.getCompanyName() + "' already exists");
        }
        
        company.setCompanyName(companyDetails.getCompanyName());
        company.setAddress(companyDetails.getAddress());
        company.setPhone(companyDetails.getPhone());
        company.setEmail(companyDetails.getEmail());
        if (companyDetails.getActive() != null) {
            company.setActive(companyDetails.getActive());
        }
        
        return companyRepository.save(company);
    }
    
    @Override
    public void delete(Long id) {
        CompanyModel company = findById(id);
        
        // Verificar si la compañía tiene departamentos o empleados asociados
        if (company.getDepartments() != null && !company.getDepartments().isEmpty()) {
            throw new RuntimeException("Cannot delete company with associated departments");
        }
        
        if (company.getEmployees() != null && !company.getEmployees().isEmpty()) {
            throw new RuntimeException("Cannot delete company with associated employees");
        }
        
        companyRepository.delete(company);
    }
    
    @Override
    public void toggleActiveStatus(Long id) {
        CompanyModel company = findById(id);
        company.setActive(!company.getActive());
        companyRepository.save(company);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String companyName) {
        return companyRepository.existsByCompanyName(companyName);
    }
}
