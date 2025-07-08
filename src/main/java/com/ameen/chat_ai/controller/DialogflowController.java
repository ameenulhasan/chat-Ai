package com.ameen.chat_ai.controller;

import com.ameen.chat_ai.dto.AiResponseDto;
import com.ameen.chat_ai.service.DialogflowService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/chatAi")
public class DialogflowController {

    private final DialogflowService dialogflowService;

    public DialogflowController(DialogflowService dialogflowService) {
        this.dialogflowService = dialogflowService;
    }

    @MessageMapping("/dialogflow-voice-command")
    @SendTo("/topic/responses")
    public AiResponseDto handleVoiceCommand(String command) {
        return dialogflowService.processCommand(command);
    }

    @PostMapping("/ai-response")
    public @ResponseBody AiResponseDto getAiResponse(@RequestParam String command) {
        return dialogflowService.processCommand(command);
    }

    @PostMapping("/send")
    public Map<String, String> chatWithAI(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String aiResponse = dialogflowService.getAIResponse(userMessage);
        return Map.of("response", aiResponse);
    }

}