package com.traini.traini_backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.traini.traini_backend.models.PrivilegeModel;

public interface PrivilegeRepository extends JpaRepository<PrivilegeModel, Long> {
    Optional<PrivilegeModel> findByName(String name);
    boolean existsByName(String name);
}
