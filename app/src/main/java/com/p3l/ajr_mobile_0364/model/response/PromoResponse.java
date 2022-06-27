package com.p3l.ajr_mobile_0364.model.response;

import com.google.gson.annotations.SerializedName;
import com.p3l.ajr_mobile_0364.model.Promo;
import com.p3l.ajr_mobile_0364.model.Transaction;

import java.util.List;

public class PromoResponse {
    private String message;
    @SerializedName("data")
    private List<Promo> promo;

    public PromoResponse(String message, List<Promo> promo) {
        this.message = message;
        this.promo = promo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Promo> getPromo() {
        return promo;
    }

    public void setPromo(List<Promo> promo) {
        this.promo = promo;
    }
}
