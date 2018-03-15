package com.redray.khadoomhome.Technical.Model;



public class Add_Parts_Items_tech {

    private String add_id;
    private  String productName;
    private  boolean checkbox;
    private  String quantity;
    private  String unit_Cost;
    private  String total_unit_Cost;

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


    public String getUnit_Cost() {
        return unit_Cost;
    }

    public void setUnit_Cost(String unit_Cost) {
        this.unit_Cost = unit_Cost;
    }


    public String getTotal_unit_Cost() {
        return total_unit_Cost;
    }

    public void setTotal_unit_Cost(String total_unit_Cost) {
        this.total_unit_Cost = total_unit_Cost;
    }

}
