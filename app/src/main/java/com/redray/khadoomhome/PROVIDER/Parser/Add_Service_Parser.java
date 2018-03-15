package com.redray.khadoomhome.PROVIDER.Parser;

import android.util.Log;

import com.redray.khadoomhome.PROVIDER.Models.Add_service_items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class Add_Service_Parser {


    private static final String JSON_ARRAY ="Data";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME= "name";


    private String json;

    public Add_Service_Parser(String json)
    {
        this.json = json;
    }


    public ArrayList<Add_service_items>  parseProducts (){

        JSONObject jsonObject ;

        ArrayList<Add_service_items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                Add_service_items products = new Add_service_items();

                JSONObject jo = productsJA.getJSONObject(i);

                products.setProductId(jo.getString(KEY_ID));
                products.setProductName(jo.getString(KEY_NAME));

                productsArrayList.add(products);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList",e.getMessage()+"");
        }

        return  productsArrayList;
    }

}