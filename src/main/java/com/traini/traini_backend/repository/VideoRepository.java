package com.traini.traini_backend.repository;

import com.traini.traini_backend.models.VideoModel;

import org.springframework.data.repository.CrudRepository;

public interface VideoRepository extends CrudRepository<VideoModel, Long> {
}