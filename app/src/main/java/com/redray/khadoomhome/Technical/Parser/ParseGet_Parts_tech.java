package com.redray.khadoomhome.Technical.Parser;

import android.util.Log;

import com.redray.khadoomhome.Technical.Model.Get_Parts_Items_tech;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class ParseGet_Parts_tech {


    private static final String JSON_ARRAY ="parts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME= "name";
    private static final String KEY_CHECKD= "checked";
    private static final String KEY_QTY= "qty";
    private static final String KEY_UNIT_COST= "unitCost";
    private static final String KEY_TOTAL_UNIT_COST= "subTotal";


    private String json;

    public ParseGet_Parts_tech(String json)
    {
        this.json = json;
    }


    public ArrayList<Get_Parts_Items_tech>  parseProducts (){

        JSONObject jsonObject ;

        ArrayList<Get_Parts_Items_tech> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                Get_Parts_Items_tech products = new Get_Parts_Items_tech();

                JSONObject jo = productsJA.getJSONObject(i);

                products.setProductId(jo.getString(KEY_ID));
                products.setProductName(jo.getString(KEY_NAME));
                products.setcheckbox(jo.getBoolean(KEY_CHECKD));
                products.setQuantity(jo.getString(KEY_QTY));
                products.setUnit_Cost(jo.getString(KEY_UNIT_COST));
                products.setTotal_unit_Cost(jo.getString(KEY_TOTAL_UNIT_COST));


                productsArrayList.add(products);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList",e.getMessage()+"");
        }

        return  productsArrayList;
    }

}