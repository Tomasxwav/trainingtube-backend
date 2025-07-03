package com.traini.traini_backend.services;



import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.traini.traini_backend.enums.Department;
import com.traini.traini_backend.models.EmployeeVideoInteractionModel;
import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.repository.EmployeeRepository;
import com.traini.traini_backend.repository.EmployeeVideoInteractionRepository;

@Service
public class EmployeeVideoInteractionService {
    @Autowired
    private EmployeeVideoInteractionRepository repository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Guardar o actualizar una interacción
    public EmployeeVideoInteractionModel saveInteraction(EmployeeVideoInteractionModel interaction) {
        interaction.setLastInteractionDate(new Date());
        return repository.save(interaction);
    }

    // Obtener interacciones de un empleado
    public List<EmployeeVideoInteractionModel> getInteractionsByEmployee(Long employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    // Marcar/desmarcar como favorito
    public EmployeeVideoInteractionModel toggleFavorite(Long employeeId, Long videoId, boolean isFavorite) {
        EmployeeVideoInteractionModel interaction = repository.findByEmployeeIdAndVideoId(employeeId, videoId);
        if (interaction == null) {
            interaction = new EmployeeVideoInteractionModel();
            interaction.setId(employeeId);
            interaction.setVideoId(videoId);
        }
        interaction.setFavorite(isFavorite);
        return saveInteraction(interaction);
    }

    // Obtener videos pendientes de un empleado
    public List<VideoModel> getPendingVideosByEmployee(Long employeeId) {
        Department department = employeeRepository.findDepartmentById(employeeId);
        return repository.findPendingVideosByEmployee(department, employeeId);
    }

    // Obtener videos favoritos de un empleado
    public List<EmployeeVideoInteractionModel> getFavoriteVideosByEmployee(Long employeeId) {
        return repository.findByEmployeeIdAndIsFavorite(employeeId, true);
    }
}