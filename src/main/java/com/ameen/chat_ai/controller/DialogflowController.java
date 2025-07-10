package com.ameen.chat_ai.controller;

import com.ameen.chat_ai.service.DialogflowService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/chatAi")
public class DialogflowController {

    private final DialogflowService dialogflowService;

    public DialogflowController(DialogflowService dialogflowService) {
        this.dialogflowService = dialogflowService;
    }

    @PostMapping("/send")
    public Map<String, String> chatWithAI(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String aiResponse = dialogflowService.getAIResponse(userMessage);
        return Map.of("response", aiResponse);
    }

}