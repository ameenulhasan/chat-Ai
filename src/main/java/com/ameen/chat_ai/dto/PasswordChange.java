package com.ameen.chat_ai.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChange {

    private String oldPassword;
    private String newPassWord;
}
