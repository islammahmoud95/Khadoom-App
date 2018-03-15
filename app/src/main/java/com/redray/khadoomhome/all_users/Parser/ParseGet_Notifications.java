package com.redray.khadoomhome.all_users.Parser;

import android.util.Log;

import com.redray.khadoomhome.all_users.Models.Get_notify_Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ParseGet_Notifications {


    private static final String JSON_ARRAY ="Data";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE= "title";
    private static final String KEY_CONTENT= "content";
    private static final String KEY_TYPE_NOTIFY= "type_notify";
    private static final String KEY_REDIRECT_ID= "redirect_id";
    private static final String KEY_NOTIFY_TIME= "date";


    private String json;
    public ParseGet_Notifications(String json)
    {
        this.json = json;
    }
    public ArrayList<Get_notify_Items>  parseProducts (){

        JSONObject jsonObject ;

        ArrayList<Get_notify_Items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);

            for (int i = 0; i < productsJA.length(); i++) {
                Get_notify_Items products = new Get_notify_Items();

                JSONObject jo = productsJA.getJSONObject(i);

                products.setNotify_id(jo.getString(KEY_ID));
                products.setTitle(jo.getString(KEY_TITLE));
                products.setContent(jo.getString(KEY_CONTENT));
                products.setNotify_Type(jo.getString(KEY_TYPE_NOTIFY));
                products.setRedirect_ID(jo.getString(KEY_REDIRECT_ID));
                products.setTime_notify(jo.getString(KEY_NOTIFY_TIME));

                productsArrayList.add(products);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList",e.getMessage()+"");
        }

        return  productsArrayList;
    }

}