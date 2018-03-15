package com.redray.khadoomhome.PROVIDER.Parser;

import android.util.Log;

import com.redray.khadoomhome.PROVIDER.Models.Get_orders_Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class ParseGet_Orders {


    private static final String JSON_ARRAY ="Data";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME= "desc";
    private static final String KEY_DATE= "date";
    private static final String KEY_SERVICE= "mainCategory";
    private static final String KEY_IMG= "image";
    private static final String KEY_LOCATION= "location";


    private String json;
    public ParseGet_Orders(String json)
    {
        this.json = json;
    }
    public ArrayList<Get_orders_Items>  parseProducts (){

        JSONObject jsonObject ;

        ArrayList<Get_orders_Items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                Get_orders_Items products = new Get_orders_Items();

                JSONObject jo = productsJA.getJSONObject(i);

                products.setOrder_Id(jo.getString(KEY_ID));
                products.setOrder_Name(jo.getString(KEY_NAME));
                products.setOrder_serv(jo.getString(KEY_SERVICE));
                products.setOrder_Location(jo.getString(KEY_LOCATION));
                products.setOrder_Img(jo.getString(KEY_IMG));
                products.setDate(jo.getString(KEY_DATE));

                productsArrayList.add(products);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList",e.getMessage()+"");
        }

        return  productsArrayList;
    }

}