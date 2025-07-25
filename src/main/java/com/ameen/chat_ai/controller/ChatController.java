package com.ameen.chat_ai.controller;

import com.ameen.chat_ai.dto.ChatResponseDto;
import com.ameen.chat_ai.response.ApiResponse;
import com.ameen.chat_ai.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> saveChat(@RequestBody ChatResponseDto chatDto) {
        return chatService.saveChat(chatDto);
    }

}
