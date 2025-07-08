package com.ameen.chat_ai.service;

import com.ameen.chat_ai.dto.AiResponseDto;

public interface DialogflowService {

    AiResponseDto processCommand(String command);

    String getAIResponse(String userMessage);

    String getDialogflowReply(String userMessage);
}
