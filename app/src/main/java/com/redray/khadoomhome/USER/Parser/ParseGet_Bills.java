package com.redray.khadoomhome.USER.Parser;

import android.util.Log;

import com.redray.khadoomhome.USER.Models.Get_Bills_Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class ParseGet_Bills {


    private static final String JSON_ARRAY ="Data";

    private static final String KEY_ID = "id";
    private static final String KEY_ORDER_ID= "order_id";
    private static final String KEY_DATE= "bill_date";
    private static final String KEY_desc= "orderDesc";
    private static final String KEY_STATUS= "status_str";
    private static final String KEY_STATUS_VALUE= "status";
    private static final String KEY_AMOUNT= "amount";


    private String json;

    public ParseGet_Bills(String json)
    {
        this.json = json;
    }


    public ArrayList<Get_Bills_Items>  parseProducts (){

        JSONObject jsonObject ;

        ArrayList<Get_Bills_Items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                Get_Bills_Items products = new Get_Bills_Items();

                JSONObject jo = productsJA.getJSONObject(i);

                products.setBill_Id(jo.getString(KEY_ID));
                products.setOrder_id(jo.getString(KEY_ORDER_ID));
                products.setOrder_Amount(jo.getString(KEY_AMOUNT));
                products.setBill_Status(jo.getString(KEY_STATUS));
                products.setBill_Status_Value(jo.getString(KEY_STATUS_VALUE));
                products.setDate(jo.getString(KEY_DATE));
                products.setBill_Detail(jo.getString(KEY_desc));

                productsArrayList.add(products);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList",e.getMessage()+"");
        }

        return  productsArrayList;
    }

}