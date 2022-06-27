package com.p3l.ajr_mobile_0364.api;

public class Api {
    public static final String BASE_URL = "https://ajr-backend.project-patricia.xyz/api/";
    public static final String BASE_URL_IMG = "https://ajr-backend.project-patricia.xyz/storage/public/";

//    public static final String BASE_URL = "http://10.0.3.2:8000/api/";
//    public static final String BASE_URL_IMG = "http://10.0.3.2:8000/storage/";

    public static final String LOGIN = BASE_URL + "login";
    public static final String LOGOUT = BASE_URL + "logout";
    public static final String GET_CUST_BY_ID = BASE_URL + "user/";
    public static final String GET_PROMO = BASE_URL + "listpromo";
    public static final String GET_CAR = BASE_URL + "listcar";
    public static final String GET_CAR_BY_ID = BASE_URL + "dataCar/";
    public static final String GET_MGR_BY_ID = BASE_URL + "employee/";
    public static final String GET_DRV_BY_ID = BASE_URL + "driverprofile/";
    public static final String GET_UPCOMING_ORDER = BASE_URL + "upcomingOrder/";
    public static final String GET_DRIVER_ORDER = BASE_URL + "driverTransaction/";
    public static final String GET_NEWEST_RATE = BASE_URL + "newestRate/";
    public static final String GET_INCOME_REPORT = BASE_URL + "incomeReport/";
    public static final String GET_DRIVER_REPORT = BASE_URL + "driverReport/";
    public static final String GET_CAR_REPORT = BASE_URL + "carReport/";
    public static final String GET_TOP_DRIVER_REPORT = BASE_URL + "topDriver/";
    public static final String GET_TOP_CUST_REPORT = BASE_URL + "topCust/";
    public static final String UPDATE_CUST = BASE_URL + "user";
    public static final String UPDATE_DRIVER = BASE_URL + "driver";
    public static final String UPDATE_AVAILABILITY = BASE_URL + "updateAvailability";
    public static final String UPDATE_AVERAGE = BASE_URL + "updateAverage";
}
