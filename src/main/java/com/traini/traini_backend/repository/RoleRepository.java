package com.traini.traini_backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.traini.traini_backend.enums.Role;
import com.traini.traini_backend.models.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> findByName(Role name);
    boolean existsByName(Role name);
}