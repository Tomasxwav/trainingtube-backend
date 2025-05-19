package com.traini.traini_backend.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    @Autowired
    private Storage storage;

    @Value("${firebase.bucket-name}")
    private String bucketName;

    public String uploadVideo(MultipartFile file) throws IOException {
        // Genera un nombre único para el archivo
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Configura el BlobId y BlobInfo
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        // Sube el archivo a Firebase Storage
        Blob blob = storage.create(blobInfo, file.getBytes());

        // Retorna la URL pública del archivo (asegúrate de que los permisos del bucket lo permitan)
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }
}