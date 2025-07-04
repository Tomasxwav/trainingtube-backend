package com.traini.traini_backend.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.traini.traini_backend.enums.Role;
import com.traini.traini_backend.models.PrivilegeModel;
import com.traini.traini_backend.models.RoleModel;
import com.traini.traini_backend.repository.PrivilegeRepository;
import com.traini.traini_backend.repository.RoleRepository;


@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRolesAndPrivileges(
        PrivilegeRepository privilegeRepository,
        RoleRepository roleRepository
    ) {
        return _ -> {
            // Verificar y crear privilegios solo si no existen
            
            // ADMIN PRIVILEGES
            PrivilegeModel adminVideos = createPrivilegeIfNotFound("ADMIN_VIDEOS", privilegeRepository);
            PrivilegeModel adminDepartamentos = createPrivilegeIfNotFound("ADMIN_DEPARTMENTS", privilegeRepository);
            PrivilegeModel adminEmpleados = createPrivilegeIfNotFound("ADMIN_EMPLOYEES", privilegeRepository);
            PrivilegeModel viewAllMetrics = createPrivilegeIfNotFound("VIEW_ALL_METRICS", privilegeRepository);

            // SUPERVISOR PRIVILEGES
            PrivilegeModel adminDepartmentVideos = createPrivilegeIfNotFound("ADMIN_DEPARTMENT_VIDEOS", privilegeRepository);
            PrivilegeModel adminDepartmentEmployees = createPrivilegeIfNotFound("ADMIN_DEPARTMENT_EMPLOYEES", privilegeRepository);
            PrivilegeModel viewDepartmentMetrics = createPrivilegeIfNotFound("VIEW_DEPARTMENT_METRICS", privilegeRepository);

            // EMPLOYEE PRIVILEGES
            PrivilegeModel viewVideosDepartment = createPrivilegeIfNotFound("VIEW_VIDEOS_DEPARTMENT", privilegeRepository);
            PrivilegeModel viewMyMetrics = createPrivilegeIfNotFound("VIEW_MY_METRICS", privilegeRepository);
            PrivilegeModel viewMyInteractions = createPrivilegeIfNotFound("VIEW_MY_INTERACTIONS", privilegeRepository);
            PrivilegeModel comment = createPrivilegeIfNotFound("COMMENT", privilegeRepository);
            PrivilegeModel like = createPrivilegeIfNotFound("LIKE", privilegeRepository);
            PrivilegeModel favorites = createPrivilegeIfNotFound("FAVORITES", privilegeRepository);

            // Verificar y crear roles solo si no existen
            createRoleIfNotFound(
                Role.ADMIN,
                Set.of(adminVideos, adminDepartamentos, adminEmpleados, 
                    viewAllMetrics, viewMyMetrics, viewMyInteractions,
                    comment, like, favorites),
                roleRepository
            );

            createRoleIfNotFound(
                Role.SUPERVISOR,
                Set.of(adminDepartmentVideos, adminDepartmentEmployees, 
                    viewDepartmentMetrics, viewMyMetrics, 
                    viewMyInteractions, comment, like, favorites),
                roleRepository
            );

            createRoleIfNotFound(
                Role.EMPLOYEE,
                Set.of(viewVideosDepartment, viewMyMetrics, 
                    viewMyInteractions, comment, like, favorites),
                roleRepository
            );
        };
    }

    private PrivilegeModel createPrivilegeIfNotFound(String name, PrivilegeRepository privilegeRepository) {
        PrivilegeModel privilege = privilegeRepository.findByName(name).orElse(null);
        if (privilege == null) {
            privilege = new PrivilegeModel(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    private void createRoleIfNotFound(Role roleName, Set<PrivilegeModel> privileges, RoleRepository roleRepository) {
        RoleModel role = roleRepository.findByName(roleName).orElse(null);
        if (role == null) {
            role = new RoleModel(roleName, privileges);
            roleRepository.save(role);
        }
    }
}