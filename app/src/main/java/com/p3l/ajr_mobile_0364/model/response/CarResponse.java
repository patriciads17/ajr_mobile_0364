package com.p3l.ajr_mobile_0364.model.response;

import com.google.gson.annotations.SerializedName;
import com.p3l.ajr_mobile_0364.model.Car;
import com.p3l.ajr_mobile_0364.model.Report;

import java.util.List;

public class CarResponse {
    private String message;
    @SerializedName("data")
    private Car car;

    @SerializedName("data_array")
    private List<Car> carList;

    public CarResponse(String message, Car car) {
        this.message = message;
        this.car = car;
    }

    public CarResponse(String message, List<Car> carList) {
        this.message = message;
        this.carList = carList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }
}
