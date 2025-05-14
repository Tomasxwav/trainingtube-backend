package com.traini.traini_backend.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
@ConfigurationProperties(prefix = "firebase")
public class FirebaseConfig {
 

    @Bean
    public FirebaseApp initializFirebase() throws IOException {
        String serviceAccountPath = System.getProperty("user.dir") + "/src/main/resources/serviceAccountKey.json";
        FileInputStream serviceAccountStream = new FileInputStream(serviceAccountPath);

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
            .setStorageBucket("gs://trainidb.appspot.com/tutoriales")
            .build();

        return FirebaseApp.initializeApp(options);
    }

    

   /*  
    
    // Configuración para Admin SDK (opcional)
    private AdminConfig admin;

    // Getters y Setters (IMPORTANTE para @ConfigurationProperties)
   
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    
    // ... otros getters/setters ...

    // Clase interna para Admin SDK
    public static class AdminConfig {
        private String serviceAccount;
        private String databaseUrl;

        // Getters y Setters
        public String getServiceAccount() { return serviceAccount; }
        public void setServiceAccount(String serviceAccount) { this.serviceAccount = serviceAccount; }
        public String getDatabaseUrl() { return databaseUrl; }
        public void setDatabaseUrl(String databaseUrl) { this.databaseUrl = databaseUrl; }
    } */
}