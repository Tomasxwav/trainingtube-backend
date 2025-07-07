package com.traini.traini_backend.services;

import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;


import com.traini.traini_backend.enums.Department;
import com.traini.traini_backend.models.InteractionModel;
import com.traini.traini_backend.models.VideoModel;
import com.traini.traini_backend.repository.EmployeeRepository;
import com.traini.traini_backend.repository.InteractionRepository;

@Service
public class InteractionService {
    @Autowired
    private InteractionRepository repository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Guardar o actualizar una interacción
    public InteractionModel saveInteraction(InteractionModel interaction) {
        interaction.setLastInteractionDate(new Date());
        return repository.save(interaction);
    }

    // Obtener interacciones de un empleado
    public List<InteractionModel> getInteractionsByEmployee(Long employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    // Marcar/desmarcar como favorito
    public InteractionModel toggleFavorite(Long employeeId, Long videoId, boolean isFavorite) {
        InteractionModel interaction = repository.findByEmployeeIdAndVideoId(employeeId, videoId);
        if (interaction == null) {
            interaction = new InteractionModel();
            interaction.setId(employeeId);
            interaction.setVideoId(videoId);
        }
        interaction.setFavorite(isFavorite);
        return saveInteraction(interaction);
    }

    // Obtener videos pendientes de un empleado
    public List<VideoModel> getPendingVideos(Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();

        Department department = employeeRepository.findDepartmentById(employeeId);
        return repository.findPendingVideosByEmployee(department, employeeId);
    }

    // Obtener videos favoritos del empleado actual
    public List<VideoModel> getFavoriteVideos(Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();
        return repository.findFavoritesVideosByEmployee(employeeId);
        
    }

    // Marcar/desmarcar como favorito
    public InteractionModel toggleFavorite(Long id, Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();
        InteractionModel interaction = repository.findByEmployeeIdAndVideoId(employeeId, id);
        if (interaction == null) {
            interaction = new InteractionModel();
            interaction.setId(employeeId);
            interaction.setVideoId(id);
        }
        interaction.setFavorite(!interaction.isFavorite());
        return repository.save(interaction);
    }

    // Marcar/desmarcar como pendiente
    public InteractionModel togglePending(Long id, Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();
        InteractionModel interaction = repository.findByEmployeeIdAndVideoId(employeeId, id);
        if (interaction == null) {
            interaction = new InteractionModel();
            interaction.setId(employeeId);
            interaction.setVideoId(id);
        }
        interaction.setPending(!interaction.isPending());
        return repository.save(interaction);
    }

    // Marcar/desmarcar como favorito
    public InteractionModel toggleLike(Long id, Authentication authentication) {
        String email = authentication.getName();
        Long employeeId = employeeRepository.findByEmail(email).get().getId();
        InteractionModel interaction = repository.findByEmployeeIdAndVideoId(employeeId, id);
        if (interaction == null) {
            interaction = new InteractionModel();
            interaction.setId(employeeId);
            interaction.setVideoId(id);
        }
        interaction.setWatched(!interaction.isWatched());
        return repository.save(interaction);
    }
}