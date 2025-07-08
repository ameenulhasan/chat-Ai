package com.ameen.chat_ai.controller;

import com.ameen.chat_ai.service.AiProcessingService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatAi")
public class AiProcessingController {

    private final AiProcessingService aiProcessingService;

    public AiProcessingController(AiProcessingService aiProcessingService) {
        this.aiProcessingService = aiProcessingService;
    }
    @MessageMapping("/home-command")
    @SendTo("/topicResponses")
    public String handleHomeCommand(String command) {
        return aiProcessingService.processHomeCommand(command);
    }

    @GetMapping("/command")
    public String processCommand(@RequestParam String command) {
        return aiProcessingService.processHomeCommand(command);
    }

}

