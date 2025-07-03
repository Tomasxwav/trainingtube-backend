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
            // Crear privilegios

            // ADMIN PRIVILEGES
            PrivilegeModel adminVideos = new PrivilegeModel("ADMIN_VIDEOS");
            PrivilegeModel adminDepartamentos = new PrivilegeModel("ADMIN_DEPARTMENTS");
            PrivilegeModel adminEmpleados = new PrivilegeModel("ADMIN_EMPLOYEES");
            PrivilegeModel viewAllMetrics = new PrivilegeModel("VIEW_ALL_METRICS");

            // SUPERVISOR PRIVILEGES
            PrivilegeModel adminDepartmentVideos = new PrivilegeModel("ADMIN_DEPARTMENT_VIDEOS");
            PrivilegeModel adminDepartmentEmployees = new PrivilegeModel("ADMIN_DEPARTMENT_EMPLOYEES");
            PrivilegeModel viewDepartmentMetrics = new PrivilegeModel("VIEW_DEPARTMENT_METRICS");

            // EMPLOYEE PRIVILEGES
            PrivilegeModel viewVideosDepartment = new PrivilegeModel("VIEW_VIDEOS_DEPARTMENT");
            PrivilegeModel viewMyMetrics = new PrivilegeModel("VIEW_MY_METRICS");
            PrivilegeModel viewMyInteractions = new PrivilegeModel("VIEW_MY_INTERACTIONS");
            PrivilegeModel comment = new PrivilegeModel("COMMENT");
            PrivilegeModel like = new PrivilegeModel("LIKE");
            PrivilegeModel favorites = new PrivilegeModel("FAVORITES");

            privilegeRepository.saveAll(Set.of(
                adminVideos, adminDepartamentos, adminEmpleados, viewAllMetrics,
                viewVideosDepartment, adminDepartmentVideos, adminDepartmentEmployees, viewDepartmentMetrics,
                viewMyMetrics, viewMyInteractions, comment, like, favorites
            ));

            // Crear roles
            RoleModel adminRole = new RoleModel(
                Role.ADMIN,
                Set.of(adminVideos, adminDepartamentos, adminEmpleados, 
                viewAllMetrics, viewMyMetrics, viewMyInteractions,
                comment, like, favorites)
            );

            RoleModel supervisorRole = new RoleModel(
                Role.SUPERVISOR,
                Set.of(adminDepartmentVideos, adminDepartmentEmployees, 
                    viewDepartmentMetrics, viewAllMetrics, viewMyMetrics, 
                    viewMyInteractions, comment, like, favorites)
            );

            RoleModel employeeRole = new RoleModel(
                Role.EMPLOYEE,
                Set.of(viewVideosDepartment, viewMyMetrics, viewMyInteractions, comment, like, favorites )
            );

            roleRepository.save(adminRole);
            roleRepository.save(employeeRole);
            roleRepository.save(supervisorRole);
        };
    }
}