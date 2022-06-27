package com.p3l.ajr_mobile_0364.model.response;

import com.google.gson.annotations.SerializedName;
import com.p3l.ajr_mobile_0364.model.User;

public class DriverResponse {
    private String message;
    @SerializedName("data")
    private User user;
    private Integer amount_order;

    public DriverResponse(String message, User user, Integer amount_order) {
        this.message = message;
        this.user = user;
        this.amount_order = amount_order;
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

    public Integer getAmount_order() {
        return amount_order;
    }

    public void setAmount_order(Integer amount_order) {
        this.amount_order = amount_order;
    }
}
