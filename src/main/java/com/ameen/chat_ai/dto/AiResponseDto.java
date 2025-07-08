package com.ameen.chat_ai.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AiResponseDto {

    private String textResponse;
    private byte[] voiceResponse;

    public AiResponseDto(String textResponse, byte[] voiceResponse) {
        this.textResponse = textResponse;
        this.voiceResponse = voiceResponse;
    }
}
