package com.ameen.chat_ai.response;

import com.ameen.chat_ai.dto.UserTokenDto;

public class UserContextHolder {

    private UserContextHolder() {
    }
    private static final ThreadLocal<UserTokenDto> USER_CONTEXT = new ThreadLocal<>();

    public static void setUserTokenDto(UserTokenDto userDto) {
        USER_CONTEXT.set(userDto);
    }

    public static UserTokenDto getUserTokenDto() {
        return USER_CONTEXT.get();
    }

    public static void clear() {
        USER_CONTEXT.remove();
    }

}
