package com.traini.traini_backend.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FirebaseService {

    private final Storage storage;
    private final String bucketName;

    public FirebaseService() throws IOException {
        try (InputStream serviceAccount = getClass().getResourceAsStream("/serviceAccountKey.json")) {
            if (serviceAccount == null) {
                throw new IOException("No se encontró el archivo serviceAccountKey.json en el classpath");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("trainidb.appspot.com") 
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            this.bucketName = options.getStorageBucket();
            this.storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(
                    getClass().getResourceAsStream("/serviceAccountKey.json")))
                .build()
                .getService();
        }
    }

    public String uploadFile(MultipartFile file, String directoryPath) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String fullPath = directoryPath + "/" + fileName;
        
        BlobId blobId = BlobId.of(bucketName, fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(file.getContentType())
            .build();
        
        storage.create(blobInfo, file.getBytes());
        
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fullPath);
    }
}