package com.kakjziblog.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.kakjziblog.api.exception.KakjziblogException;
import com.kakjziblog.api.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {

        // ver 1
//        FieldError fieldError = e.getFieldError();
//        String field = fieldError.getField();
//        String defaultMessage = fieldError.getDefaultMessage();
//
//        Map<String, String> response = new HashMap<>();
//        response.put(field, defaultMessage);
//        return response;

        //ver2
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errorResponse;
    }

    @ResponseBody
    @ExceptionHandler(KakjziblogException.class)
    public ResponseEntity<ErrorResponse> kakjziblogException(KakjziblogException e) {
        /**
         * TODO
         * 1. Exception 의 종류에 따라 에러코드 반환해야함
         * 2. InvalidRequest 일 경우 어떤 내용이 잘못되었는 지 처리해야 함.
         */

        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(statusCode)
                .body(body);
    }

    // exception 이 추가될때마다 여기다가 추가하기에는 너무 많이 늘어나게 된다.
    // -> 상위 exception 을 만들자

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e) {
        log.error("Exception", e);

        ErrorResponse body = ErrorResponse.builder()
                .code("500")
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}
