package com.redray.khadoomhome.PROVIDER.Models;



public class Get_Parts_view_Items {

    private String add_id;
    private  String productName;
    private  String quantity;


    public String getProductId() {
        return add_id;
    }

    public void setProductId(String Add_id) {
        this.add_id = Add_id;
    }



    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }




    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
