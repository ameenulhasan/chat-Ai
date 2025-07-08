package com.ameen.chat_ai.exception;

import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {

    private ResponseEntityBuilder() {
    }

    public static ResponseEntity<Object> build(ApiError apiError){

        return new ResponseEntity<>( apiError, apiError.getStatus( ) );
    }
}

