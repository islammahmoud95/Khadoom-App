package com.redray.khadoomhome.USER.Parser;

import android.util.Log;

import com.redray.khadoomhome.USER.Models.Get_Requests_Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class ParseGet_Requests {


    private static final String JSON_ARRAY ="Data";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME= "desc";
    private static final String KEY_DATE= "date";
    private static final String KEY_TYPE= "req_type";
    private static final String KEY_STATUS= "status";
    private static final String KEY_Main_SERV= "mainCategory";


    private String json;

    public ParseGet_Requests(String json)
    {
        this.json = json;
    }


    public ArrayList<Get_Requests_Items>  parseProducts (){

        JSONObject jsonObject ;

        ArrayList<Get_Requests_Items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                Get_Requests_Items products = new Get_Requests_Items();

                JSONObject jo = productsJA.getJSONObject(i);

                products.setRequest_Id(jo.getString(KEY_ID));
                products.setRequest_Name(jo.getString(KEY_NAME));
                products.setRequest_Type(jo.getString(KEY_TYPE));
                products.setRequest_Status(jo.getString(KEY_STATUS));
                products.setDate(jo.getString(KEY_DATE));
                products.setMainService(jo.getString(KEY_Main_SERV));

                productsArrayList.add(products);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList",e.getMessage()+"");
        }

        return  productsArrayList;
    }

}