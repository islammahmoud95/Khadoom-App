package com.redray.khadoomhome.USER.Models;



public class Get_Parts_Items {

    private String add_id;
    private  String productName;
    private  boolean checkbox;
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



    public boolean getcheckbox() {
        return checkbox;
    }

    public void setcheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
