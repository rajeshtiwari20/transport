package com.jaijobner.transport_new.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private Boolean status;
    private String message;
    private T data;

    private ApiResponse(Boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Success without data
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<T>(Boolean.TRUE, message, null);
    }

    // Success with data
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(Boolean.TRUE, message, data);
    }

    // Failure
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(Boolean.FALSE, message, null);
    }
}
