package com.ameen.chat_ai.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {

    private Long id;
    private String userName;
    private String emailId;
    private String phoneNumber;
    private String address;
    private String dateOfBirth;
    private String gender;
    private String password;

}
