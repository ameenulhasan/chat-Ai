package com.ameen.chat_ai.serviceImpl;

import com.ameen.chat_ai.dto.AiResponseDto;
import com.ameen.chat_ai.service.DialogflowService;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DialogflowServiceImpl implements DialogflowService {

    @Value("${ai.api.url}")
    private String aiApiUrl;

    @Value("${ai.api.key}")
    private String aiApiKey;

    @Value("${tts.api.url}")
    private String ttsApiUrl;

    private final RestTemplate restTemplate;
    private final SessionsClient sessionsClient;

    public DialogflowServiceImpl(RestTemplate restTemplate, SessionsClient sessionsClient) {
        this.restTemplate = restTemplate;
        this.sessionsClient = sessionsClient;
    }

    @Override
    public AiResponseDto processCommand(String command) {
        String textResponse = getTextResponseFromAI(command);
        byte[] voiceResponse = generateSpeech(textResponse);
        return new AiResponseDto(textResponse, voiceResponse);
    }
    private String getTextResponseFromAI(String command) {
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("prompt", command);
            requestBody.put("max_tokens", "50");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + aiApiKey);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(aiApiUrl, HttpMethod.POST, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();  // Debug log
            return "AI response failed: " + e.getMessage();
        }
    }
    private byte[] generateSpeech(String text) {
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("text", text);
            requestBody.put("voice", "en-US");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + aiApiKey);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(ttsApiUrl, HttpMethod.POST, entity, byte[].class);
            return response.getBody();
        } catch (Exception e) {
            return new byte[0];
        }
    }

    private static final String AI_API_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api.key}")
    private String apiKey;

    @Override
    public String getAIResponse(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        // Request payload
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "You are an AI assistant."),
                Map.of("role", "user", "content", userMessage)
        ));
        requestBody.put("max_tokens", 100);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(AI_API_URL, HttpMethod.POST, request, Map.class);
            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return "Error: Unauthorized. Please check your API key.";
            }
            // Extract response safely
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty() && choices.get(0).containsKey("message")) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return "Error: Unauthorized. Please check your API key.";
            }
            return "HTTP Error: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        return "Sorry, I couldn't generate a response.";
    }

    private static final String PROJECT_ID = "dialogflow-service-454906";

    @Override
    public String getDialogflowReply(String userMessage) {
        try {
            String sessionId = UUID.randomUUID().toString();
            SessionName session = SessionName.of(PROJECT_ID, sessionId);

            TextInput textInput = TextInput.newBuilder()
                    .setText(userMessage)
                    .setLanguageCode("en-US")
                    .build();

            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

            DetectIntentRequest request = DetectIntentRequest.newBuilder()
                    .setSession(session.toString())
                    .setQueryInput(queryInput)
                    .build();

            DetectIntentResponse response = sessionsClient.detectIntent(request);
            return response.getQueryResult().getFulfillmentText();

        } catch (Exception e) {
            System.err.println("Dialogflow API Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

}