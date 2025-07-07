package com.traini.traini_backend.models;

import java.util.Date;

import com.traini.traini_backend.enums.Department;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "videos")
public class VideoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Column(name = "url_video")
    private String videoUrl;  // URL de Firebase Storage para el video

    @Column(name = "url_thumbnail")
    private String thumbnailUrl;  // URL de Firebase Storage para el thumbnail

    @Column(name = "upload_date")
    private Date uploadDate;

    @Column(name = "department")
    @Enumerated(EnumType.STRING)
    private Department department;  

    public VideoModel() {
    }

    public VideoModel(String title, String description, String videoUrl, String thumbnailUrl, Date uploadDate, Department department) {
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.uploadDate = uploadDate;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }


}