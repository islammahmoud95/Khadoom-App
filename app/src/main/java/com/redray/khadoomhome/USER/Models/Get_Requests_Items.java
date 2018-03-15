package com.redray.khadoomhome.USER.Models;



public class Get_Requests_Items {

    private String request_id;
    private String requestName;
    private String mainService;
    private String date;
    private String request_Type;
    private String request_Status;



    public String getrequest_Id() {
        return request_id;
    }

    public void setRequest_Id(String Add_id) {
        this.request_id = Add_id;
    }



    public String getRequest_Name() {
        return requestName;
    }

    public void setRequest_Name(String RequestName) {
        this.requestName = RequestName;
    }



    public String getMain_service() {
        return mainService;
    }

    public void setMainService(String main_service) {
        this.mainService = main_service;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getRequest_Type() {
        return request_Type;
    }

    public void setRequest_Type(String request_Type) {
        this.request_Type = request_Type;
    }



    public String getRequest_Status() {
        return request_Status;
    }

    public void setRequest_Status(String request_status) {
        this.request_Status = request_status;
    }

}
