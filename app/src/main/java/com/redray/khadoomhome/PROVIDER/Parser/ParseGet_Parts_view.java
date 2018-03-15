package com.redray.khadoomhome.PROVIDER.Parser;

import android.util.Log;

import com.redray.khadoomhome.PROVIDER.Models.Get_Parts_view_Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class ParseGet_Parts_view {


    private static final String JSON_ARRAY = "parts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_QTY = "qty";


    private String json;

    public ParseGet_Parts_view(String json) {
        this.json = json;
    }


    public ArrayList<Get_Parts_view_Items> parseProducts() {

        JSONObject jsonObject ;

        ArrayList<Get_Parts_view_Items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                Get_Parts_view_Items products = new Get_Parts_view_Items();

                JSONObject jo = productsJA.getJSONObject(i);

                products.setProductId(jo.getString(KEY_ID));
                products.setProductName(jo.getString(KEY_NAME));
                products.setQuantity(jo.getString(KEY_QTY));


                productsArrayList.add(products);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList", e.getMessage() + "");
        }

        return productsArrayList;
    }

}