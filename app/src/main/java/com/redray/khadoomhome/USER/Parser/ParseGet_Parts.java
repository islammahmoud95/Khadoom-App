package com.redray.khadoomhome.USER.Parser;

import android.util.Log;

import com.redray.khadoomhome.USER.Models.Get_Parts_Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class ParseGet_Parts {


    private static final String JSON_ARRAY ="Data";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME= "name";
    private static final String KEY_CHECKD= "checked";
    private static final String KEY_QTY= "qty";


    private String json;

    public ParseGet_Parts(String json)
    {
        this.json = json;
    }


    public ArrayList<Get_Parts_Items>  parseProducts (){

        JSONObject jsonObject ;

        ArrayList<Get_Parts_Items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                Get_Parts_Items products = new Get_Parts_Items();

                JSONObject jo = productsJA.getJSONObject(i);

                products.setProductId(jo.getString(KEY_ID));
                products.setProductName(jo.getString(KEY_NAME));
                products.setcheckbox(jo.getBoolean(KEY_CHECKD));
                products.setQuantity(jo.getString(KEY_QTY));



                productsArrayList.add(products);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList",e.getMessage()+"");
        }

        return  productsArrayList;
    }

}