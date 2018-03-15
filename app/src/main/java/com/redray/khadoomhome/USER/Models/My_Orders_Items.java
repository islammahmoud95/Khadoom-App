package com.redray.khadoomhome.USER.Models;


public class My_Orders_Items {

    private String ordersId;
    private String ordersName;
    private String image_order;


    public String getPOrdersId() {
        return ordersId;
    }

    public void setProductId(String orders_Id) {
        this.ordersId = orders_Id;
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
}




