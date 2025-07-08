package com.ameen.chat_ai.service;

import com.ameen.chat_ai.dto.*;
import com.ameen.chat_ai.response.ApiResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<ApiResponse> createUser(UserDto userDto);

    ResponseEntity<ApiResponse> getUserId(Long id);

    ResponseEntity<ApiResponse> deleteId(Long id);

    void changesPassword(PasswordChange passwordChange);

    void forgotPassword(ForgotPassword request) throws MessagingException;

    void verifyOtp(VerifyOtpRequest req);

    void updatePassword(UpdatePassword request);

}
