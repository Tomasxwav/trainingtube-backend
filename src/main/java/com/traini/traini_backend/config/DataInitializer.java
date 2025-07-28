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
import com.traini.traini_backend.models.CompanyModel;
import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.repository.PrivilegeRepository;
import com.traini.traini_backend.repository.RoleRepository;
import com.traini.traini_backend.repository.CompanyRepository;
import com.traini.traini_backend.repository.DepartmentRepository;
import com.traini.traini_backend.repository.EmployeeRepository;


@Configuration
public class DataInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${super.admin.email}")
    private String superAdminEmail;

    @Value("${super.admin.password}")
    private String superAdminPassword;

    @Value("${super.admin.name}")
    private String superAdminName;



    @Bean
    CommandLineRunner initRolesAndPrivileges(
        PrivilegeRepository privilegeRepository,
        RoleRepository roleRepository,
        DepartmentRepository departmentRepository,
        EmployeeRepository employeeRepository,
        CompanyRepository companyRepository
    ) {
        return _ -> {

            // Inicializar companias primero
            initializeCompanies(companyRepository);

            // Inicializar departamentos
            initializeDepartments(departmentRepository, companyRepository);
            
            // Verificar y crear privilegios solo si no existen
            // SUPER_ADMIN PRIVILEGES
            PrivilegeModel superAdminCompanies = createPrivilegeIfNotFound("canAdminCompanies", privilegeRepository);
            PrivilegeModel superAdminDepartments = createPrivilegeIfNotFound("canAdminDepartments", privilegeRepository);
            PrivilegeModel superAdminEmployees = createPrivilegeIfNotFound("canAdminEmployees", privilegeRepository);
            PrivilegeModel superAdminVideos = createPrivilegeIfNotFound("canAdminVideos", privilegeRepository);

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

            createRoleIfNotFound(Role.SUPER_ADMIN, Set.of(superAdminCompanies, superAdminDepartments, superAdminEmployees, superAdminVideos), roleRepository);


            // Inicializar usuarios
            initializeUsers(employeeRepository, departmentRepository, roleRepository);
        };
    }

    private void initializeCompanies(CompanyRepository companyRepository) {
        createCompanyIfNotFound("Local Solutions", "Calle A, 123", "8180181660", "localsolutions@prueba.com", companyRepository);
        createCompanyIfNotFound("Kobrex", "Calle B, 123", "8121700345", "kobrex@prueba.com", companyRepository); 
        System.out.println("Companias creadas");
    }

    private void initializeDepartments(DepartmentRepository departmentRepository, CompanyRepository companyRepository) {
        CompanyModel localSol = companyRepository.findByCompanyName("Local Solutions").orElse(null);
        CompanyModel kobrex = companyRepository.findByCompanyName("Kobrex").orElse(null);

        // Crear departamentos de Local Solutions
        if (localSol != null) {
            createDepartmentIfNotFound("Ventas", "Departamento de Ventas", departmentRepository, localSol);
            createDepartmentIfNotFound("Marketing", "Departamento de Marketing", departmentRepository, localSol);
            createDepartmentIfNotFound("Desarrollo", "Departamento de Desarrollo", departmentRepository, localSol);
            createDepartmentIfNotFound("Soporte", "Departamento de Soporte", departmentRepository, localSol);
            createDepartmentIfNotFound("Diseño", "Departamento de Diseño", departmentRepository, localSol);
            createDepartmentIfNotFound("Gestión", "Departamento de Gestión", departmentRepository, localSol);
            createDepartmentIfNotFound("Otros", "Otros departamentos", departmentRepository, localSol);
            System.out.println("Local Solutions departamentos creados");
        }

        // Crear departamentos de Kobrex
        if (kobrex != null) {
            createDepartmentIfNotFound("Sales", "Departamento de Ventas", departmentRepository, kobrex);
            createDepartmentIfNotFound("Marketing", "Departamento de Marketing", departmentRepository, kobrex);
            createDepartmentIfNotFound("Desarrollo", "Departamento de Desarrollo", departmentRepository, kobrex);
            createDepartmentIfNotFound("Soporte", "Departamento de Soporte", departmentRepository, kobrex);
            createDepartmentIfNotFound("Diseño", "Departamento de Diseño", departmentRepository, kobrex);
            createDepartmentIfNotFound("Gestión", "Departamento de Gestión", departmentRepository, kobrex);
            createDepartmentIfNotFound("Otros", "Otros departamentos", departmentRepository, kobrex);
            System.out.println("Kobrex departamentos creados");
        }
    }

    private void createCompanyIfNotFound(String companyName, String address, String phone, String email, CompanyRepository companyRepository) {
        if (!companyRepository.existsByCompanyName(companyName)) {
            CompanyModel company = new CompanyModel(companyName, address, phone, email);
            companyRepository.save(company);
        }
    }
    private void createDepartmentIfNotFound(String name, String description, DepartmentRepository departmentRepository, CompanyModel company) {
        if (!departmentRepository.existsByNameAndCompany(name, company)) {
            DepartmentModel department = new DepartmentModel(name, description, company);
            departmentRepository.save(department);
        }
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

    private void initializeUsers(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository) {
        createSuperAdminIfNotFound(superAdminEmail, superAdminName, superAdminPassword, Role.SUPER_ADMIN, employeeRepository, roleRepository);
        
        createUserIfNotFound("tomas@prueba.com", "admin", "Prueba123", Role.ADMIN, (long) 1, employeeRepository, departmentRepository, roleRepository);
        createUserIfNotFound("fer@prueba.com", "superadmin", "Prueba123", Role.ADMIN, (long) 8, employeeRepository, departmentRepository, roleRepository);
        createUserIfNotFound("alain@prueba.com", "employee", "Prueba123", Role.EMPLOYEE, (long) 8, employeeRepository, departmentRepository, roleRepository);
    }

    public void createSuperAdminIfNotFound(String email, String name, String password, Role role, EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        if (!employeeRepository.existsByEmail(email)) {
            RoleModel roleModel = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + role));

            EmployeeModel employee = new EmployeeModel(
                name,
                email,
                passwordEncoder.encode(password),
                roleModel,
                null
            );

            employeeRepository.save(employee);
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
