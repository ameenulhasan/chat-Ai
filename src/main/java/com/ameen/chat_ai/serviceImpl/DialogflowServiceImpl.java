package com.ameen.chat_ai.serviceImpl;

import com.ameen.chat_ai.service.DialogflowService;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DialogflowServiceImpl implements DialogflowService {

    private static final String PROJECT_ID = "your-dialogflow-project-id";
    private static final String LANGUAGE_CODE = "en";

    @Override
    public String getAIResponse(String userMessage) {
        String sessionId = UUID.randomUUID().toString();

        try (SessionsClient sessionsClient = SessionsClient.create()) {
            SessionName session = SessionName.of(PROJECT_ID, sessionId);

            TextInput.Builder textInput = TextInput.newBuilder().setText(userMessage).setLanguageCode(LANGUAGE_CODE);
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

            DetectIntentRequest request = DetectIntentRequest.newBuilder()
                    .setSession(session.toString())
                    .setQueryInput(queryInput)
                    .build();

            DetectIntentResponse response = sessionsClient.detectIntent(request);
            QueryResult queryResult = response.getQueryResult();

            return queryResult.getFulfillmentText();
        } catch (IOException e) {
            e.printStackTrace();
            return "Dialogflow Error: " + e.getMessage();
        }
    }
}
