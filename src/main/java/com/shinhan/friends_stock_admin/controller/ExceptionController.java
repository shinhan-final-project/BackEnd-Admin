package com.shinhan.friends_stock_admin.controller;

import com.shinhan.friends_stock_admin.DTO.auth.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<Response> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error(e.getMessage()));
    }
}
