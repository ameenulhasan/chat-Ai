package com.ameen.chat_ai.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private int status;
    private String message;
    private Object data;
    private String errorMessage;

    private Long totalRecordCount;
    private Integer totalPageCount;
    private Boolean hasNext;
    private Boolean  hasPrevious;

    public ApiResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public ApiResponse(int status, String message, Object data, Long totalRecordCount, Boolean hasNext, Boolean hasPrevious) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.totalRecordCount = totalRecordCount;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    public ApiResponse(int status, String message, String errorMessage) {
        this.status = status;
        this.message = message;
        this.errorMessage = errorMessage;
    }

    public ApiResponse() {
    }

}
