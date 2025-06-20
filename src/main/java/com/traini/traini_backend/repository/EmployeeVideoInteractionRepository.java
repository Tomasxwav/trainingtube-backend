package com.traini.traini_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.traini.traini_backend.models.EmployeeVideoInteractionModel;

public interface EmployeeVideoInteractionRepository 
    extends JpaRepository<EmployeeVideoInteractionModel, Long> {


     List<EmployeeVideoInteractionModel> findByEmployeeId(Long employeeId);
    
    boolean existsByEmployeeIdAndVideoIdAndIsFavorite(Long employeeId, Long videoId, boolean isFavorite);

    // Buscar una interacción específica por empleado y video
    EmployeeVideoInteractionModel findByEmployeeIdAndVideoId(Long employeeId, Long videoId);

    // Obtener todos los videos PENDIENTES de un empleado
    List<EmployeeVideoInteractionModel> findByEmployeeIdAndIsPending(Long employeeId, boolean isPending);

    // Obtener todos los videos FAVORITOS de un empleado
    List<EmployeeVideoInteractionModel> findByEmployeeIdAndIsFavorite(Long employeeId, boolean isFavorite);


}