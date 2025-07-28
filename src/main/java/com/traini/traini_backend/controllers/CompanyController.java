package com.traini.traini_backend.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traini.traini_backend.dto.company.CompanyDto;
import com.traini.traini_backend.dto.company.CompanyResponseDto;
import com.traini.traini_backend.models.CompanyModel;
import com.traini.traini_backend.services.interfaces.CompanyService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/companies")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyResponseDto>> getAllCompanies() {
        List<CompanyModel> companies = companyService.findAll();
        List<CompanyResponseDto> response = companies.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CompanyResponseDto>> getActiveCompanies() {
        List<CompanyModel> companies = companyService.findAllActive();
        List<CompanyResponseDto> response = companies.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<CompanyResponseDto>> getInactiveCompanies() {
        List<CompanyModel> companies = companyService.findAllInactive();
        List<CompanyResponseDto> response = companies.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> getCompanyById(@PathVariable Long id) {
        try {
            CompanyModel company = companyService.findById(id);
            return ResponseEntity.ok(convertToResponseDto(company));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(@Valid @RequestBody CompanyDto companyDto) {
        try {
            CompanyModel company = new CompanyModel();
            company.setCompanyName(companyDto.getCompanyName());
            company.setAddress(companyDto.getAddress());
            company.setPhone(companyDto.getPhone());
            company.setEmail(companyDto.getEmail());
            company.setActive(companyDto.getActive() != null ? companyDto.getActive() : true);

            CompanyModel createdCompany = companyService.save(company);
            return ResponseEntity.ok(convertToResponseDto(createdCompany));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> updateCompany(@PathVariable Long id, @Valid @RequestBody CompanyDto companyDto) {
        try {
            CompanyModel companyDetails = new CompanyModel();
            companyDetails.setCompanyName(companyDto.getCompanyName());
            companyDetails.setAddress(companyDto.getAddress());
            companyDetails.setPhone(companyDto.getPhone());
            companyDetails.setEmail(companyDto.getEmail());
            if (companyDto.getActive() != null) {
                companyDetails.setActive(companyDto.getActive());
            }

            CompanyModel updatedCompany = companyService.update(id, companyDetails);
            return ResponseEntity.ok(convertToResponseDto(updatedCompany));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        try {
            companyService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<CompanyResponseDto> toggleCompanyStatus(@PathVariable Long id) {
        try {
            companyService.toggleActiveStatus(id);
            CompanyModel company = companyService.findById(id);
            return ResponseEntity.ok(convertToResponseDto(company));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private CompanyResponseDto convertToResponseDto(CompanyModel company) {
        CompanyResponseDto dto = new CompanyResponseDto();
        dto.setId(company.getId());
        dto.setCompanyName(company.getCompanyName());
        dto.setAddress(company.getAddress());
        dto.setPhone(company.getPhone());
        dto.setEmail(company.getEmail());
        dto.setActive(company.getActive());
        dto.setCreatedAt(company.getCreatedAt());
        dto.setUpdatedAt(company.getUpdatedAt());
        return dto;
    }
}