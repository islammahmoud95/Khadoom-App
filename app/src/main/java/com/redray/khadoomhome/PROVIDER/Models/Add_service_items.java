package com.redray.khadoomhome.PROVIDER.Models;



public class Add_service_items {

    private String add_id;
    private  String productName;
    private  boolean checkbox ;



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




}
