package com.traini.traini_backend.services;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {
    @Value("${firebase.storage.bucket-name}")
    private String bucketName;

    private final Storage storage;

    public FirebaseStorageService(Storage storage) {
        this.storage = storage;
    }

    public String uploadVideo(MultipartFile file) throws IOException {
        return uploadFile(file, "videos/");
    }

    public String uploadThumbnail(MultipartFile file) throws IOException {
        return uploadFile(file, "thumbnails/");
    }

    private String uploadFile(MultipartFile file, String folder) throws IOException {
        String fileName = folder + UUID.randomUUID() + "_" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());
        return String.format(
            "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
            bucketName,
            java.net.URLEncoder.encode(fileName, "UTF-8")
        );
    }
}
