package com.redray.khadoomhome.USER.Models;


public class Get_Bills_Items {

    private String bill_id;
    private String order_id;
    private String date;
    private String bill_detail;
    private String bill_amount;
    private String bill_Status;
    private String bill_Status_Value;


    public String getBill_Id() {
        return bill_id;
    }

    public void setBill_Id(String bill_id) {
        this.bill_id = bill_id;
    }



    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getBill_Detail() {
        return bill_detail;
    }

    public void setBill_Detail(String bill_detail) {
        this.bill_detail = bill_detail;
    }


    public String getOrder_Amount() {
        return bill_amount;
    }

    public void setOrder_Amount(String bill_amount) {
        this.bill_amount = bill_amount;
    }


    public String getBill_Status() {
        return bill_Status;
    }

    public void setBill_Status(String bill_status) {
        this.bill_Status = bill_status;
    }



    public String getBill_Status_Value() {
        return bill_Status_Value;
    }

    public void setBill_Status_Value(String bill_Status_Value) {
        this.bill_Status_Value = bill_Status_Value;
    }

}
