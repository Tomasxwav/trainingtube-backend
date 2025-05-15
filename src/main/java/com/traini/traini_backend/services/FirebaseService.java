package com.traini.traini_backend.services;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

@Service
public class FirebaseService {
    public void uploadVideo(MultipartFile file, String title) throws IOException {
        InputStream inputStream = file.getInputStream();

        Bucket bucket = StorageClient.getInstance().bucket(/* "gs://trainidb.appspot.com/tutoriales" */);

        bucket.create(title, inputStream, "video/mp4");


        
    }
}
