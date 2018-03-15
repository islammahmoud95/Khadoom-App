package com.redray.khadoomhome.PROVIDER.Models;




public class Get_orders_Items {

    private String order_id;
    private String order_img;
    private String orderName;
    private String orderService;
    private String orderLocation;
    private String date;



    public String getOrder_Id() {
        return order_id;
    }

    public void setOrder_Id(String order_id) {
        this.order_id = order_id;
    }


    public String getOrder_Img() {
        return order_img;
    }

    public void setOrder_Img(String order_img) {
        this.order_img = order_img;
    }


    public String getOrder_Name() {
        return orderName;
    }

    public void setOrder_Name(String orderName) {
        this.orderName = orderName;
    }


    public String getOrder_serv() {
        return orderService;
    }

    public void setOrder_serv(String orderService) {
        this.orderService = orderService;
    }


    public String getOrder_Location() {
        return orderLocation;
    }

    public void setOrder_Location(String orderLocation) {
        this.orderLocation = orderLocation;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
