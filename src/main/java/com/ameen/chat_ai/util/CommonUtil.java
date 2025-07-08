package com.ameen.chat_ai.util;

import com.ameen.chat_ai.constants.Constant;
import com.ameen.chat_ai.response.ApiResponse;
import com.ameen.chat_ai.response.PageResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;

@Component
public class CommonUtil {

    private static final Random random = new Random();

    private CommonUtil() {
    }

    public static ApiResponse getApiResponse(int status, String message) {
        return new ApiResponse(status, Constant.FAILED, message);
    }

    public static ResponseEntity<ApiResponse> getResponseForError(int status, String message) {
        return ResponseEntity.ok(getApiResponse(status, message));
    }

    public static <T> ResponseEntity<ApiResponse> getOkResponse(T data) {
        return ResponseEntity.ok(getApiResponse(HttpStatus.OK.value(), Constant.SUCCESS, data));
    }

    public static <T> ApiResponse getApiResponse(int status, String message, T data) {
        return new ApiResponse(status, message, data);
    }

    public static <T> ApiResponse getApiResponse(int status, String message, Page<T> page) {
        ApiResponse apiResponse = new ApiResponse(status, message, page.getContent());
        apiResponse.setHasNext(page.hasNext());
        apiResponse.setHasPrevious(page.hasPrevious());
        apiResponse.setTotalRecordCount(page.getTotalElements());
        apiResponse.setTotalPageCount(page.getTotalPages());
        return apiResponse;
    }

    public static ResponseEntity<ApiResponse> getApiResponsePage(PageResponse<?> pageResponse) {
        ApiResponse apiResponse = new ApiResponse(
                200,
                Constant.SUCCESS,
                pageResponse.getData(),
                pageResponse.getTotalRecordCount(),
                pageResponse.isHasNext(),
                pageResponse.isHasPrevious()
        );
        return ResponseEntity.ok(apiResponse);
    }

    public static <T> ResponseEntity<ApiResponse> createPageResponse(Page<T> page) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonUtil.getApiResponsePage(HttpStatus.OK.value(), Constant.SUCCESS, page));
    }

    public static <T> ApiResponse getApiResponsePage(int status, String message, Page<T> page) {
        ApiResponse apiResponse = new ApiResponse(status, message, page.getContent());
        apiResponse.setHasNext(page.hasNext());
        apiResponse.setHasPrevious(page.hasPrevious());
        apiResponse.setTotalRecordCount(page.getTotalElements());
        apiResponse.setTotalPageCount(page.getTotalPages());
        return apiResponse;
    }

    public static String generateOTP(int len) {
        String numbers = "0123456789";
        char[] otp = new char[len];
        for (int i = 0; i < len; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return new String(otp);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);
    }

    public static String randomAlphanumeric(int count){
        return CommonUtil.randomString("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz", count);
    }

    private static String randomString(String input, int count){
        char[] charArr = input.toCharArray();
        StringBuilder builder = new StringBuilder();
        int len = charArr.length;
        for(int i=0;i<count;i++){
            int index = random.nextInt(len);
            builder.append(charArr[index]);
        }
        return builder.toString();
    }
    public static <T> void doValidation(T data){
        try(var validatorFactory = Validation.buildDefaultValidatorFactory()){
            var validator = validatorFactory.getValidator();
            Set<ConstraintViolation<T>> violationSet = validator.validate(data);
            if(!violationSet.isEmpty()){
                throw new ConstraintViolationException(violationSet);
            }
        }
    }
}
