package com.redray.khadoomhome.USER.Models;


public class My_Orders_Sub_Items {

    private String orders_main_Id;
    private String ordersId;
    private String ordersName;
    private String image_order;
    private String order_fees;

    public String getOrdersId() {
        return ordersId;
    }

    public void setProductId(String orders_Id) {
        this.ordersId = orders_Id;
    }


    public String getOrders_main_Id() {
        return orders_main_Id;
    }

    public void setOrder_main_Id(String orders_main_Id) {
        this.orders_main_Id = orders_main_Id;
    }


    public String getOrdersName() {
        return ordersName;
    }

    public void setOrdersName(String orders_Name) {
        this.ordersName = orders_Name;
    }


    public String getimage_Order() {
        return image_order;
    }

    public void setimage_Order(String image_order) {
        this.image_order = image_order;
    }


    public String getorder_fees() {
        return order_fees;
    }

    public void setOrder_fees(String order_fees) {
        this.order_fees = order_fees;
    }
}




