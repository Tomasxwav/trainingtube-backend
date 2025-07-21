package com.traini.traini_backend.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.traini.traini_backend.enums.Role;
import com.traini.traini_backend.models.PrivilegeModel;
import com.traini.traini_backend.models.RoleModel;
import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.repository.PrivilegeRepository;
import com.traini.traini_backend.repository.RoleRepository;
import com.traini.traini_backend.repository.DepartmentRepository;


@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRolesAndPrivileges(
        PrivilegeRepository privilegeRepository,
        RoleRepository roleRepository,
        DepartmentRepository departmentRepository
    ) {
        return _ -> {
            // Inicializar departamentos primero
            initializeDepartments(departmentRepository);
            
            // Verificar y crear privilegios solo si no existen
            
            // ADMIN PRIVILEGES
            PrivilegeModel adminVideos = createPrivilegeIfNotFound("canAdminVideos", privilegeRepository);
            PrivilegeModel adminDepartments = createPrivilegeIfNotFound("canAdminDepartments", privilegeRepository);
            PrivilegeModel adminEmployees = createPrivilegeIfNotFound("canAdminEmployees", privilegeRepository);
            PrivilegeModel viewAllMetrics = createPrivilegeIfNotFound("canViewAllMetrics", privilegeRepository);

            // SUPERVISOR PRIVILEGES
            PrivilegeModel adminDepartmentVideos = createPrivilegeIfNotFound("canAdminDepartmentVideos", privilegeRepository);
            PrivilegeModel adminDepartmentEmployees = createPrivilegeIfNotFound("canAdminDepartmentEmployees", privilegeRepository);
            PrivilegeModel viewDepartmentMetrics = createPrivilegeIfNotFound("canViewDepartmentMetrics", privilegeRepository);

            // EMPLOYEE PRIVILEGES
            PrivilegeModel viewVideosDepartment = createPrivilegeIfNotFound("canViewVideosDepartment", privilegeRepository);
            PrivilegeModel viewMyMetrics = createPrivilegeIfNotFound("canViewMyMetrics", privilegeRepository);
            PrivilegeModel viewMyInteractions = createPrivilegeIfNotFound("canViewMyInteractions", privilegeRepository);
            PrivilegeModel comment = createPrivilegeIfNotFound("canComment", privilegeRepository);
            PrivilegeModel like = createPrivilegeIfNotFound("canLike", privilegeRepository);
            PrivilegeModel favorites = createPrivilegeIfNotFound("canFavorites", privilegeRepository);

            // Verificar y crear roles solo si no existen
            createRoleIfNotFound(
                Role.ADMIN,
                Set.of(adminVideos, adminDepartments, adminEmployees, 
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
    
    private void initializeDepartments(DepartmentRepository departmentRepository) {
        // Crear departamentos básicos si no existen
        createDepartmentIfNotFound("Ventas", "Departamento de Ventas", departmentRepository);
        createDepartmentIfNotFound("Marketing", "Departamento de Marketing", departmentRepository);
        createDepartmentIfNotFound("Desarrollo", "Departamento de Desarrollo", departmentRepository);
        createDepartmentIfNotFound("Soporte", "Departamento de Soporte", departmentRepository);
        createDepartmentIfNotFound("Diseño", "Departamento de Diseño", departmentRepository);
        createDepartmentIfNotFound("Gestión", "Departamento de Gestión", departmentRepository);
        createDepartmentIfNotFound("Otros", "Otros departamentos", departmentRepository);
    }
    
    private void createDepartmentIfNotFound(String name, String description, DepartmentRepository departmentRepository) {
        if (!departmentRepository.existsByName(name)) {
            DepartmentModel department = new DepartmentModel(name, description);
            departmentRepository.save(department);
        }
    }
}