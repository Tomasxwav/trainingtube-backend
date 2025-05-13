package com.traini.traini_backend.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.traini.traini_backend.models.VideoModel;


@Repository
public interface VideoRepository extends CrudRepository<VideoModel, Long> {
    Optional<VideoModel> findByTitle(String title);
}



