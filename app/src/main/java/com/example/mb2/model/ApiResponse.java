package com.example.mb2.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}