package com.traini.traini_backend.repository;

import com.traini.traini_backend.models.DepartmentModel;
import com.traini.traini_backend.models.VideoModel;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface VideoRepository extends CrudRepository<VideoModel, Long> {
    List<VideoModel> findByDepartment(DepartmentModel department);
}