package com.example.techapp.model;

import com.google.gson.annotations.SerializedName;

public class ResponseModel<T> {

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data;

    public ResponseModel() {
    }

    public ResponseModel(boolean error, String message, T data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
