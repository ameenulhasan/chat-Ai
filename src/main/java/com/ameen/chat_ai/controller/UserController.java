package com.ameen.chat_ai.controller;

import com.ameen.chat_ai.dto.*;
import com.ameen.chat_ai.response.ApiResponse;
import com.ameen.chat_ai.service.UserService;
import com.ameen.chat_ai.util.CommonUtil;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password changed successfully";
    private static final String SENT_TO_YOUR_REGISTERED_EMAIL = "OTP sent to your registered email";
    private static final String VERIFIED_SUCCESSFULLY = "OTP verified successfully";
    private static final String PASSWORD_UPDATED_SUCCESSFULLY = "Password updated successfully";

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserDto userDto) {
        return service.createUser(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable Long id) {
        return service.getUserId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        return service.deleteId(id);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody PasswordChange passwordChange) {
        service.changesPassword(passwordChange);
        return CommonUtil.getOkResponse(PASSWORD_CHANGED_SUCCESSFULLY);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody ForgotPassword request) throws MessagingException {
        service.forgotPassword(request);
        return CommonUtil.getOkResponse(SENT_TO_YOUR_REGISTERED_EMAIL);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody VerifyOtpRequest req) {
        service.verifyOtp(req);
        return CommonUtil.getOkResponse(VERIFIED_SUCCESSFULLY);
    }

    @PostMapping("/update-password")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody UpdatePassword request) {
        service.updatePassword(request);
        return CommonUtil.getOkResponse(PASSWORD_UPDATED_SUCCESSFULLY);
    }

}
