package com.traini.traini_backend.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.traini.traini_backend.dto.employee.UpdateEmployeeDto;
import com.traini.traini_backend.enums.Role;
import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.models.InteractionModel;
import com.traini.traini_backend.models.RoleModel;
import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.models.CompanyModel;
import com.traini.traini_backend.repository.EmployeeRepository;
import com.traini.traini_backend.repository.InteractionRepository;
import com.traini.traini_backend.repository.RoleRepository;
import com.traini.traini_backend.repository.VideoRepository;
import com.traini.traini_backend.repository.CompanyRepository;
import com.traini.traini_backend.repository.DepartmentRepository;
import com.traini.traini_backend.services.interfaces.EmployeeService;
import com.traini.traini_backend.config.TenantContext;

@Service
public class EmployeeServiceImpl implements UserDetailsService, EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private InteractionRepository interactionRepository;
    
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        EmployeeModel employee = employeeRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
            employee.getEmail(),
            employee.getPassword(),
            employee.getRole().getAuthorities()
        );
    }

    public boolean existsByEmail(String email){
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public List<EmployeeModel> findAll() {
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null) {
            return employeeRepository.findByCompanyIdExcludingSuperAdmin(tenantId);
        }
        return employeeRepository.findAllExcludingSuperAdmin();
    }


    @Override
    public EmployeeModel findById(Long id) {
        return employeeRepository.findById(id).orElseThrow( () -> new IllegalArgumentException(String.format("The employee with id %s not found.", id)) );
    }

    @Override
    public List<EmployeeModel> findByDepartment(Authentication authentication) {
        String email = authentication.getName();
        EmployeeModel employee = employeeRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        return employeeRepository.findByDepartment(employee.getDepartment());
    }

    @Override
    public EmployeeModel save(EmployeeModel employee) {
        // Auto-asignar company si hay contexto de tenant y el empleado no tiene company asignada
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null && employee.getCompany() == null) {
            CompanyModel company = companyRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
            employee.setCompany(company);
        }
        
        EmployeeModel savedEmployee = employeeRepository.save(employee);
        assignDepartmentVideosAsPending(savedEmployee);
        return savedEmployee;
    }


    @Override
    public EmployeeModel update(Long id, UpdateEmployeeDto updateRequest) {
        EmployeeModel employeeFound = findById(id);

        if (updateRequest.getName() != null) {
            employeeFound.setName(updateRequest.getName());
        }
        if (updateRequest.getEmail() != null) {
            employeeFound.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPassword() != null) {
            employeeFound.setPassword(updateRequest.getPassword());
        }
        if (updateRequest.getRole() != null) {
            Role roleEnum = updateRequest.getRoleEnum();
            Optional<RoleModel> roleModel = roleRepository.findByName(roleEnum);
            if (roleModel.isPresent()) {
                employeeFound.setRole(roleModel.get());
            } else {
                throw new RuntimeException("Role not found: " + updateRequest.getRole());
            }
        }
        if (updateRequest.getDepartmentId() != null) {
            DepartmentModel department = departmentRepository.findById(updateRequest.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + updateRequest.getDepartmentId()));
            employeeFound.setDepartment(department);
        }

        return employeeRepository.save(employeeFound);
    }

    @Override
    public EmployeeModel delete(Long id) {
        EmployeeModel employeeFound = findById(id);
        employeeRepository.deleteById(id);
        return employeeFound;
    }

    @Override
    public EmployeeModel saveAsSupervisor(EmployeeModel employee, Authentication authentication) {
        String email = authentication.getName();
        EmployeeModel supervisor = employeeRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Supervisor no encontrado con email: " + email));
        
        if (employee.getDepartment() != null && !employee.getDepartment().getId().equals(supervisor.getDepartment().getId())) {
            throw new RuntimeException("No tienes permisos para crear empleados en este departamento. Solo puedes crear empleados en tu propio departamento.");
        }
        
        if (employee.getDepartment() == null) {
            employee.setDepartment(supervisor.getDepartment());
        }
        
        // Auto-asignar company si hay contexto de tenant y el empleado no tiene company asignada
        Long tenantId = TenantContext.getCurrentTenant();
        if (tenantId != null && employee.getCompany() == null) {
            CompanyModel company = companyRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
            employee.setCompany(company);
        }
        
        EmployeeModel savedEmployee = employeeRepository.save(employee);
        assignDepartmentVideosAsPending(savedEmployee);
        return savedEmployee;
    }

    
    @Override
    public EmployeeModel updateAsSupervisor(Long id, UpdateEmployeeDto updateRequest, Authentication authentication) {
        String email = authentication.getName();
        EmployeeModel supervisor = employeeRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Supervisor no encontrado con email: " + email));
        
        EmployeeModel employeeFound = findById(id);
        
        if (!employeeFound.getDepartment().getId().equals(supervisor.getDepartment().getId())) {
            throw new RuntimeException("No tienes permisos para actualizar este empleado. Solo puedes actualizar empleados de tu propio departamento.");
        }
        
        if (updateRequest.getDepartmentId() != null && !updateRequest.getDepartmentId().equals(supervisor.getDepartment().getId())) {
            throw new RuntimeException("No puedes transferir empleados a otro departamento. Solo puedes mantener empleados en tu propio departamento.");
        }

        if (updateRequest.getName() != null) {
            employeeFound.setName(updateRequest.getName());
        }
        if (updateRequest.getEmail() != null) {
            employeeFound.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPassword() != null) {
            employeeFound.setPassword(updateRequest.getPassword());
        }
        if (updateRequest.getRole() != null) {
            Role roleEnum = updateRequest.getRoleEnum();
            Optional<RoleModel> roleModel = roleRepository.findByName(roleEnum);
            if (roleModel.isPresent()) {
                employeeFound.setRole(roleModel.get());
            } else {
                throw new RuntimeException("Role not found: " + updateRequest.getRole());
            }
        }
        if (updateRequest.getDepartmentId() != null) {
            DepartmentModel department = departmentRepository.findById(updateRequest.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + updateRequest.getDepartmentId()));
            employeeFound.setDepartment(department);
        }

        return employeeRepository.save(employeeFound);
    }

    @Override
    public EmployeeModel deleteAsSupervisor(Long id, Authentication authentication) {
        String email = authentication.getName();
        EmployeeModel supervisor = employeeRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Supervisor no encontrado con email: " + email));
        
        EmployeeModel employeeFound = findById(id);
        
        if (!employeeFound.getDepartment().getId().equals(supervisor.getDepartment().getId())) {
            throw new RuntimeException("No tienes permisos para eliminar este empleado. Solo puedes eliminar empleados de tu propio departamento.");
        }
        
        employeeRepository.deleteById(id);
        return employeeFound;
    }

    private void assignDepartmentVideosAsPending(EmployeeModel employee) {
        DepartmentModel employeeDepartment = employee.getDepartment();

        List<VideoModel> departmentVideos = videoRepository.findByDepartment(employeeDepartment);

        departmentVideos.forEach(video -> {
            InteractionModel interaction = new InteractionModel();
            interaction.setEmployee(employee);
            interaction.setVideoId(video.getId());
            interaction.setPending(true); // Marcar como pendiente
            interaction.setLastInteractionDate(new Date());

            interactionRepository.save(interaction);
        });
    }

    public EmployeeModel getUserByEmail(String email) {
        return employeeRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

}
