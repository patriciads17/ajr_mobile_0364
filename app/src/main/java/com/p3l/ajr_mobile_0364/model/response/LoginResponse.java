package com.p3l.ajr_mobile_0364.model.response;

import com.google.gson.annotations.SerializedName;
import com.p3l.ajr_mobile_0364.model.User;

public class LoginResponse {
    private String message;
    private String token_type;
    private String access_token;
    private String token_scope;
    @SerializedName("user")
    private User user;

    public LoginResponse(String message, String token_type, String access_token, String token_scope, User user) {
        this.message = message;
        this.token_type = token_type;
        this.access_token = access_token;
        this.token_scope = token_scope;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_scope() {
        return token_scope;
    }

    public void setToken_scope(String token_scope) {
        this.token_scope = token_scope;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
