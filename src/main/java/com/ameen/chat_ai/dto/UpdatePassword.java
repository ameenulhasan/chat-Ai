package com.ameen.chat_ai.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdatePassword {

    private String email;
    private String newPassword;

}
