package com.ameen.chat_ai.service;

import com.ameen.chat_ai.dto.ChatResponseDto;
import com.ameen.chat_ai.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ChatService {

    ResponseEntity<ApiResponse> saveChat(ChatResponseDto chatDto);

}
