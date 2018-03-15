package com.redray.khadoomhome.USER.Parser;

import android.util.Log;

import com.redray.khadoomhome.USER.Models.Get_rating_Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class ParseGet_Rating {


    private static final String JSON_ARRAY ="standards";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_RATE_NUM = "rate_num";


    private String json;

    public ParseGet_Rating(String json)
    {
        this.json = json;
    }


    public ArrayList<Get_rating_Items>  parseProducts (){

        JSONObject jsonObject ;

        ArrayList<Get_rating_Items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                Get_rating_Items products = new Get_rating_Items();

                JSONObject jo = productsJA.getJSONObject(i);

                products.setRate_Id(jo.getString(KEY_ID));
                products.setRate_name(jo.getString(KEY_NAME));
                products.setRate_Num(jo.getDouble(KEY_RATE_NUM));

                productsArrayList.add(products);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList",e.getMessage()+"");
        }

        return  productsArrayList;
    }

}