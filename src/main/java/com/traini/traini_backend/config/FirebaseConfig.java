package com.traini.traini_backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.bucket-name}") //  application.properties
    private String bucketName;

    @PostConstruct
    public void initialize() throws IOException {

        ClassPathResource serviceAccount = new ClassPathResource("serviceAccountKey.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                .setStorageBucket(bucketName)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    @Bean
    public Storage storage() throws IOException {
        ClassPathResource serviceAccount = new ClassPathResource("serviceAccountKey.json");
        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                .build()
                .getService();
    }
}