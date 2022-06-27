package com.p3l.ajr_mobile_0364.model.response;

import com.google.gson.annotations.SerializedName;
import com.p3l.ajr_mobile_0364.model.User;

public class CustomerResponse {
    private String message;
    @SerializedName("data")
    private User user;

    public CustomerResponse(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
