package com.example.softwareProjectWithDocker.exception;

import com.example.softwareProjectWithDocker.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({EngineerNotFoundException.class})
    public ResponseEntity<ApiResponse<Object>> handleException(EngineerNotFoundException e) {
        return ResponseEntity.badRequest().body(ApiResponse.error(null, e.getMessage()));
    }
    


}
