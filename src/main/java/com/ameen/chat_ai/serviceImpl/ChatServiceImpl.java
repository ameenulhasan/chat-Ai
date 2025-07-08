package com.ameen.chat_ai.serviceImpl;

import com.ameen.chat_ai.constants.Constant;
import com.ameen.chat_ai.dto.ChatResponseDto;
import com.ameen.chat_ai.model.Chat;
import com.ameen.chat_ai.repository.ChatRepository;
import com.ameen.chat_ai.repository.UserRepository;
import com.ameen.chat_ai.response.ApiResponse;
import com.ameen.chat_ai.response.UserContextHolder;
import com.ameen.chat_ai.service.ChatService;
import com.ameen.chat_ai.service.DialogflowService;
import com.ameen.chat_ai.util.CommonUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.ExecutorService;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final DialogflowService dialogflowService;
    private final ExecutorService executorService;  // Background Thread Pool

    public ChatServiceImpl(ChatRepository chatRepository, UserRepository userRepository, DialogflowService dialogflowService, ExecutorService executorService) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.dialogflowService = dialogflowService;
        this.executorService = executorService;
    }

    @Override
    public ResponseEntity<ApiResponse> saveChat(ChatResponseDto chatDto) {

        Long userId = UserContextHolder.getUserTokenDto().getId();
        Chat chat = new Chat();
        chat.setUserMessage(chatDto.getUserMessage());
        chat.setAiModel("Dialogflow");
        chat.setStatus(Chat.ChatStatus.SENT);
        chat.setIsActive(true);
        chat.setDeletedFlag(false);
        chat.setCreatedAt(Timestamp.from(Instant.now()));
        chat.setCreatedBy(userId);
        Chat savedChat = chatRepository.save(chat);
        executorService.submit(() -> autoReplyAi(savedChat));
        return CommonUtil.getOkResponse(Constant.CHAT);
    }
    private void autoReplyAi(Chat chat) {
        try {
            String aiResponse = dialogflowService.getDialogflowReply(chat.getUserMessage());
            chat.setReplyAiMessage(aiResponse);
            chat.setStatus(Chat.ChatStatus.DELIVERED);
            chatRepository.save(chat);  // Update AI Reply in DB
        } catch (Exception e) {
            chat.setStatus(Chat.ChatStatus.FAILED);
            chatRepository.save(chat);
        }
    }

}
