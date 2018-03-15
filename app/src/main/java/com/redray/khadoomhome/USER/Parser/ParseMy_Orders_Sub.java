package com.redray.khadoomhome.USER.Parser;

import android.util.Log;

import com.redray.khadoomhome.USER.Models.My_Orders_Sub_Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ParseMy_Orders_Sub {


    private static final String JSON_ARRAY ="Data";

    private static final String KEY_Main_ID = "category_id";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME= "name";
    private static final String KEY_Image= "image";
    private static final String KEY_Fees= "visit_fees";

    private String json;

    public ParseMy_Orders_Sub(String json)
    {
        this.json = json;
    }


    public ArrayList<My_Orders_Sub_Items>  parseProducts (){

        JSONObject jsonObject ;

        ArrayList<My_Orders_Sub_Items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                My_Orders_Sub_Items products = new My_Orders_Sub_Items();

                JSONObject jo = productsJA.getJSONObject(i);

                products.setOrder_main_Id(jo.getString(KEY_Main_ID));
                products.setProductId(jo.getString(KEY_ID));
                products.setOrdersName(jo.getString(KEY_NAME));
                products.setimage_Order(jo.getString(KEY_Image));
                products.setOrder_fees(jo.getString(KEY_Fees));

                productsArrayList.add(products);

            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList",e.getMessage()+"");

        }

        return  productsArrayList;
    }

}