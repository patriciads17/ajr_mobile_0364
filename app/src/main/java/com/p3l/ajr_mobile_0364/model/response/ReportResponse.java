package com.p3l.ajr_mobile_0364.model.response;

import com.google.gson.annotations.SerializedName;
import com.p3l.ajr_mobile_0364.model.Report;

import java.util.List;

public class ReportResponse {
    private String message;
    @SerializedName("data")
    private List<Report> reportList;

    public ReportResponse(String message, List<Report> reportList) {
        this.message = message;
        this.reportList = reportList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Report> getReportList() {
        return reportList;
    }

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
    }
}
