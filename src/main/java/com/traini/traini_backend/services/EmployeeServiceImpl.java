package com.traini.traini_backend.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.traini.traini_backend.models.EmployeeModel;
import com.traini.traini_backend.models.InteractionModel;
import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.repository.EmployeeRepository;
import com.traini.traini_backend.repository.InteractionRepository;
import com.traini.traini_backend.repository.VideoRepository;
import com.traini.traini_backend.services.interfaces.EmployeeService;

@Service
public class EmployeeServiceImpl implements UserDetailsService, EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private InteractionRepository interactionRepository;

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
        return (List<EmployeeModel>) employeeRepository.findAll();
    }


    @Override
    public EmployeeModel findById(Long id) {
        return employeeRepository.findById(id).orElseThrow( () -> new IllegalArgumentException(String.format("The employee with id %s not found.", id)) );
    }

    @Override
    public EmployeeModel save(EmployeeModel employee) {
        EmployeeModel savedEmployee = employeeRepository.save(employee);
        assignDepartmentVideosAsPending(savedEmployee);
        return savedEmployee;
    }

    @Override
    public EmployeeModel update(Long id, EmployeeModel employee) {
        EmployeeModel employeeFound = findById(id);

        if( employee.getName() != null ) employeeFound.setName(employee.getName());
        if( employee.getEmail() != null ) employeeFound.setEmail(employee.getEmail());
        if( employee.getPassword() != null ) employeeFound.setPassword(employee.getPassword());
        if( employee.getRole() != null ) employeeFound.setRole(employee.getRole());

        return employeeRepository.save(employeeFound);
    }

    @Override
    public EmployeeModel delete(Long id) {
        EmployeeModel employeeFound = findById(id);
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

}
