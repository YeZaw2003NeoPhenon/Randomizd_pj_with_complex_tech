package com.example.softwareProjectWithDocker.response;

import jakarta.annotation.Nullable;

public class ApiResponse<T>{

    private T data;
    private String message;
    private boolean isSuccess;
    private ErrorResponse error;


    public ApiResponse() {}

    public static <T> ApiResponse<T> success(@Nullable T data){

        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        response.setSuccess(true);
        return response;
    }

    public static <T> ApiResponse<T> success(T data, String message){
        ApiResponse<T> response = success(data);
        response.setMessage(message);
        return response;
    }

    public static <T> ApiResponse<T> error(@Nullable T data){
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        response.setSuccess(false);
        return response;
    }

    public static <T> ApiResponse<T> error(T data, String message){
        ApiResponse<T> response = success(data);
        response.setMessage(message);
        response.setError(new ErrorResponse(-1 ,message,false));
        return response;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    public static class ErrorResponse{
        private int count;
        private String message;
        private boolean success;
        public ErrorResponse(int count, String message, boolean success) {
            this.count = count;
            this.message = message;
            this.success = success;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}