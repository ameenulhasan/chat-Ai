package com.ameen.chat_ai.controller;

import com.ameen.chat_ai.dto.LoginDto;
import com.ameen.chat_ai.util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDto loginDto) {
        return jwtUtil.login(loginDto);
    }

    @PostMapping("/refreshToken")
    public Map<String, String> refreshToken(@RequestParam String request ) {
        return jwtUtil.refreshToken(request);
    }

}
