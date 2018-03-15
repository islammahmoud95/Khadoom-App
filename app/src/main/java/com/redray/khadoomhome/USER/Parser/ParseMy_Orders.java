package com.redray.khadoomhome.USER.Parser;

import android.util.Log;

import com.redray.khadoomhome.USER.Models.My_Orders_Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ParseMy_Orders {


    private static final String JSON_ARRAY ="Data";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME= "name";
    private static final String KEY_Image= "image";


    private String json;

    public ParseMy_Orders(String json)
    {
        this.json = json;
    }


    public ArrayList<My_Orders_Items>  parseProducts (){

        JSONObject jsonObject ;

        ArrayList<My_Orders_Items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                My_Orders_Items products = new My_Orders_Items();

                JSONObject jo = productsJA.getJSONObject(i);


                products.setProductId(jo.getString(KEY_ID));
                products.setOrdersName(jo.getString(KEY_NAME));
                products.setimage_Order(jo.getString(KEY_Image));


                productsArrayList.add(products);

            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList",e.getMessage()+"");

        }

        return  productsArrayList;
    }

}