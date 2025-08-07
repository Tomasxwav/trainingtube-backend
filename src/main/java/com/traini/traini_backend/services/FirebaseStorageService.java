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


    public String uploadThumbnail(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        Blob blob = storage.create(blobInfo, file.getBytes(), 
            Storage.BlobTargetOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ));

        return blob.getMediaLink();
    }

    public void deleteVideo(String videoUrl) throws Exception {
        try {
            String fileName = extractFileNameFromUrl(videoUrl);
            if (fileName != null) {
                BlobId blobId = BlobId.of(bucketName, fileName);
                boolean deleted = storage.delete(blobId);
                if (!deleted) {
                    System.err.println("No se pudo eliminar el video: " + fileName);
                }
            }
        } catch (Exception e) {
            throw new Exception("Error eliminando video de Firebase Storage: " + e.getMessage());
        }
    }

    public void deleteThumbnail(String thumbnailUrl) throws Exception {
        try {
            String fileName = extractFileNameFromUrl(thumbnailUrl);
            if (fileName != null) {
                BlobId blobId = BlobId.of(bucketName, fileName);
                boolean deleted = storage.delete(blobId);
                if (!deleted) {
                    System.err.println("No se pudo eliminar el thumbnail: " + fileName);
                }
            }
        } catch (Exception e) {
            throw new Exception("Error eliminando thumbnail de Firebase Storage: " + e.getMessage());
        }
    }

    private String extractFileNameFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        
        try {
            // Las URLs de Firebase Storage tienen el formato:
            // https://storage.googleapis.com/download/storage/v1/b/bucket-name/o/filename?...
            // o https://storage.googleapis.com/bucket-name/filename
            
            if (url.contains("/o/")) {
                // Formato con /o/
                String[] parts = url.split("/o/");
                if (parts.length > 1) {
                    String filenamePart = parts[1];
                    // Remover parámetros de query si existen
                    int queryIndex = filenamePart.indexOf('?');
                    if (queryIndex > 0) {
                        filenamePart = filenamePart.substring(0, queryIndex);
                    }
                    // Decodificar URL encoding
                    return java.net.URLDecoder.decode(filenamePart, "UTF-8");
                }
            } else {
                // Formato directo bucket/filename
                String[] parts = url.split(bucketName + "/");
                if (parts.length > 1) {
                    String filenamePart = parts[1];
                    // Remover parámetros de query si existen
                    int queryIndex = filenamePart.indexOf('?');
                    if (queryIndex > 0) {
                        filenamePart = filenamePart.substring(0, queryIndex);
                    }
                    return filenamePart;
                }
            }
        } catch (Exception e) {
            System.err.println("Error extrayendo nombre de archivo de la URL: " + e.getMessage());
        }
        
        return null;
    }
}