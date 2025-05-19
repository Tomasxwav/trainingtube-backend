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

@Service
public class FirebaseStorageService {

    @Autowired
    private Storage storage;

    @Value("${firebase.storage.bucket-name}")
    private String bucketName;

    public String uploadVideo(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        Blob blob = storage.create(blobInfo, file.getBytes(), 
            Storage.BlobTargetOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ));

        return blob.getMediaLink();


    }
}