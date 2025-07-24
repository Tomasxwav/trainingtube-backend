package com.traini.traini_backend.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.traini.traini_backend.enums.Role;
import com.traini.traini_backend.models.PrivilegeModel;
import com.traini.traini_backend.models.RoleModel;
import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.repository.PrivilegeRepository;
import com.traini.traini_backend.repository.RoleRepository;
import com.traini.traini_backend.repository.DepartmentRepository;
import com.traini.traini_backend.repository.EmployeeRepository;


@Configuration
public class DataInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initRolesAndPrivileges(
        PrivilegeRepository privilegeRepository,
        RoleRepository roleRepository,
        DepartmentRepository departmentRepository,
        EmployeeRepository employeeRepository
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


            // Inicializar usuarios
            initializeUsers(employeeRepository, departmentRepository, roleRepository);
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

    private void initializeUsers(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository) {
        createUserIfNotFound("tomas@prueba.com", "admin", "Prueba123", Role.ADMIN, (long) 1, employeeRepository, departmentRepository, roleRepository);
        createUserIfNotFound("superadmin@example.com", "superadmin", "Prueba123", Role.SUPERVISOR, (long) 1, employeeRepository, departmentRepository, roleRepository);
        createUserIfNotFound("employee@example.com", "employee", "employee123", Role.EMPLOYEE, (long) 1, employeeRepository, departmentRepository, roleRepository);
    }
    
    private void createDepartmentIfNotFound(String name, String description, DepartmentRepository departmentRepository) {
        if (!departmentRepository.existsByName(name)) {
            DepartmentModel department = new DepartmentModel(name, description);
            departmentRepository.save(department);
        }
    }

    private void createUserIfNotFound(String email, String name, String password, Role role, Long departmentId, EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository) {
        if (!employeeRepository.existsByEmail(email)) {
            RoleModel roleModel = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + role));
            
            DepartmentModel departmentModel = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado con ID: " + departmentId));
            
            EmployeeModel employee = new EmployeeModel(
                name,
                email,
                passwordEncoder.encode(password),
                roleModel,
                departmentModel
            );
            
            employeeRepository.save(employee);
        }
    }
}
