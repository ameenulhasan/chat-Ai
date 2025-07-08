package com.ameen.chat_ai.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserTokenDto {

    private Long id;
    private String email;
    private String userName;
    private long iat;
    private long exp;
    private String sub;
    private String role;


}
