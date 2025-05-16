package com.traini.traini_backend.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "videos")
public class VideoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;

    @Column(name = "url_video")
    private String urlVideo;  // URL de Firebase Storage para el video

    @Column(name = "url_thumbnail")
    private String urlThumbnail;  // URL de Firebase Storage para el thumbnail
}