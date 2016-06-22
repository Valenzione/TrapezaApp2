package com.trapezateam.trapeza.api.models;

/**
 * Created by ilgiz on 6/21/16.
 */
public class AuthenticationResponse {

    private boolean success;

    private String message;

    private String token;

    private int id;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", id=" + id +
                '}';
    }
}
