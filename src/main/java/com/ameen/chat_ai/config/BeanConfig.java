package com.ameen.chat_ai.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;


import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@PropertySource("classpath:application.properties")
public class BeanConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10);
    }

    @Value("${dialogflow.credentials.path}")
    private String credentialsPath;

    @Bean
    public SessionsClient sessionsClient() throws IOException {
        System.out.println("DIALOGFLOW FILE PATH => " + credentialsPath);
        InputStream credentialsStream = new ClassPathResource(credentialsPath).getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
        SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        return SessionsClient.create(sessionsSettings);
    }

    @PostConstruct
    public void checkJson() {
        try (InputStream is = new ClassPathResource("dialogflow-sa.json").getInputStream()) {
            System.out.println("DIALOGFLOW FILE SIZE => " + is.available());
        } catch (IOException e) {
            System.out.println("Failed to load JSON: " + e.getMessage());
        }
    }

}
