package com.traini.traini_backend.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.traini.traini_backend.enums.Role;
import com.traini.traini_backend.models.PrivilegeModel;
import com.traini.traini_backend.models.RoleModel;
import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.models.CompanyModel;
import com.traini.traini_backend.repository.PrivilegeRepository;
import com.traini.traini_backend.repository.RoleRepository;
import com.traini.traini_backend.repository.DepartmentRepository;
import com.traini.traini_backend.repository.EmployeeRepository;
import com.traini.traini_backend.repository.CompanyRepository;


@Configuration
public class DataInitializer {

    @Value("${super.admin.email}")
    private String superAdminEmail;

    @Value("${super.admin.password}")
    private String superAdminPassword;

    @Value("${super.admin.name}")
    private String superAdminName;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initRolesAndPrivileges(
        PrivilegeRepository privilegeRepository,
        RoleRepository roleRepository,
        DepartmentRepository departmentRepository,
        EmployeeRepository employeeRepository,
        CompanyRepository companyRepository
    ) {
        return _ -> {
            // Inicializar companies primero
            initializeCompanies(companyRepository);
            
            // Inicializar departamentos para cada company
            initializeDepartments(departmentRepository, companyRepository);
            
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

            // SUPER_ADMIN PRIVILEGES
            PrivilegeModel adminCompanies = createPrivilegeIfNotFound("canAdminCompanies", privilegeRepository);

            // Verificar y crear roles solo si no existen
            createRoleIfNotFound(
                Role.SUPER_ADMIN,
                Set.of(adminCompanies, adminVideos, adminDepartments, adminEmployees, viewAllMetrics),
                roleRepository
            );

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
            initializeUsers(employeeRepository, departmentRepository, roleRepository, companyRepository);
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
    
    private void initializeDepartments(DepartmentRepository departmentRepository, CompanyRepository companyRepository) {
        // Obtener companies
        CompanyModel techCorp = companyRepository.findByCompanyName("TechCorp Solutions").orElse(null);
        CompanyModel innovate = companyRepository.findByCompanyName("Innovate Dynamics").orElse(null);
        
        if (techCorp != null) {
            // Departamentos para TechCorp Solutions
            createDepartmentIfNotFound("Ventas", "Departamento de Ventas", techCorp, departmentRepository);
            createDepartmentIfNotFound("Marketing", "Departamento de Marketing", techCorp, departmentRepository);
            createDepartmentIfNotFound("Desarrollo", "Departamento de Desarrollo", techCorp, departmentRepository);
            createDepartmentIfNotFound("Soporte", "Departamento de Soporte", techCorp, departmentRepository);
        }
        
        if (innovate != null) {
            // Departamentos para Innovate Dynamics
            createDepartmentIfNotFound("Diseño", "Departamento de Diseño", innovate, departmentRepository);
            createDepartmentIfNotFound("Gestión", "Departamento de Gestión", innovate, departmentRepository);
            createDepartmentIfNotFound("Investigación", "Departamento de Investigación", innovate, departmentRepository);
            createDepartmentIfNotFound("Producción", "Departamento de Producción", innovate, departmentRepository);
        }
    }

    private void initializeUsers(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository, CompanyRepository companyRepository) {
        // Super Admin (no pertenece a ninguna company)
        createSuperAdminIfNotFound(superAdminEmail, superAdminName, superAdminPassword, Role.SUPER_ADMIN, employeeRepository, roleRepository);

        // Usuarios para TechCorp Solutions
        CompanyModel techCorp = companyRepository.findByCompanyName("TechCorp Solutions").orElse(null);
        if (techCorp != null) {
            DepartmentModel ventasDept = departmentRepository.findByNameAndCompany("Ventas", techCorp).orElse(null);
            DepartmentModel desarrolloDept = departmentRepository.findByNameAndCompany("Desarrollo", techCorp).orElse(null);
            
            if (ventasDept != null) {
                createUserIfNotFound("daniel@prueba.com", "Admin Daniel TechCorp", "Prueba123", Role.ADMIN, ventasDept, employeeRepository, roleRepository);
                createUserIfNotFound("alain@prueba.com", "Alain Vendedor", "Prueba123", Role.EMPLOYEE, ventasDept, employeeRepository, roleRepository);
            }
            
            if (desarrolloDept != null) {
                createUserIfNotFound("david@prueba.com", "David Supervisor", "Prueba123", Role.SUPERVISOR, desarrolloDept, employeeRepository, roleRepository);
                createUserIfNotFound("javi@prueba.com", "Javi Desarrollador", "Prueba123", Role.EMPLOYEE, desarrolloDept, employeeRepository, roleRepository);
            }
        }
        
        // Usuarios para Innovate Dynamics
        CompanyModel innovate = companyRepository.findByCompanyName("Innovate Dynamics").orElse(null);
        if (innovate != null) {
            DepartmentModel diseñoDept = departmentRepository.findByNameAndCompany("Diseño", innovate).orElse(null);
            DepartmentModel gestionDept = departmentRepository.findByNameAndCompany("Gestión", innovate).orElse(null);
            
            if (diseñoDept != null) {
                createUserIfNotFound("amanda@prueba.com", "Admin Amanda Innovate", "Prueba123", Role.ADMIN, diseñoDept, employeeRepository, roleRepository);
                createUserIfNotFound("fer@prueba.com", "Fer Diseñadora", "Prueba123", Role.EMPLOYEE, diseñoDept, employeeRepository, roleRepository);
            }
            
            if (gestionDept != null) {
                createUserIfNotFound("juan@prueba.com", "Juan Supervisor", "Prueba123", Role.SUPERVISOR, gestionDept, employeeRepository, roleRepository);
                createUserIfNotFound("liz@prueba.com", "Liz Gerente", "Prueba123", Role.EMPLOYEE, gestionDept, employeeRepository, roleRepository);
            }
        }
    }
    
    private void initializeCompanies(CompanyRepository companyRepository) {
        createCompanyIfNotFound("TechCorp Solutions", "Empresa líder en soluciones tecnológicas", 
                "Calle Principal 123, Ciudad Tech", "+1234567890", "contact@techcorp.com", companyRepository);
        createCompanyIfNotFound("Innovate Dynamics", "Empresa especializada en innovación y desarrollo", 
                "Avenida Innovación 456, Centro Empresarial", "+0987654321", "info@innovate.com", companyRepository);
    }
    
    private void createCompanyIfNotFound(String companyName, String description, String address, String phone, String email, CompanyRepository companyRepository) {
        if (!companyRepository.existsByCompanyName(companyName)) {
            CompanyModel company = new CompanyModel(companyName, address, phone, email);
            companyRepository.save(company);
        }
    }
    
    private void createDepartmentIfNotFound(String name, String description, CompanyModel company, DepartmentRepository departmentRepository) {
        if (!departmentRepository.existsByNameAndCompany(name, company)) {
            DepartmentModel department = new DepartmentModel(name, description, company);
            departmentRepository.save(department);
        }
    }
    
    private void createSuperAdminIfNotFound(String email, String name, String password, Role role, EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        if (!employeeRepository.existsByEmail(email)) {
            RoleModel roleModel = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + role));
            
            EmployeeModel employee = new EmployeeModel(
                name,
                email,
                passwordEncoder.encode(password),
                roleModel,
                (CompanyModel) null  // Super Admin no pertenece a ninguna company
            );
            
            employeeRepository.save(employee);
        }
    }
    
    private void createUserIfNotFound(String email, String name, String password, Role role, DepartmentModel department, EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        if (!employeeRepository.existsByEmail(email)) {
            RoleModel roleModel = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + role));
            
            EmployeeModel employee = new EmployeeModel(
                name,
                email,
                passwordEncoder.encode(password),
                roleModel,
                department
            );
            
            employeeRepository.save(employee);
        }
    }
}
