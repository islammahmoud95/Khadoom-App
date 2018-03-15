package com.redray.khadoomhome.tickets.parser;

import android.util.Log;

import com.redray.khadoomhome.tickets.models.Get_Tickets_Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class ParseGet_tickets {


    private static final String JSON_ARRAY ="Data";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE= "reason";
    private static final String KEY_desc= "lastReplay";
    private static final String KEY_DATE= "created_at";


    private String json;
    public ParseGet_tickets(String json)
    {
        this.json = json;
    }
    public ArrayList<Get_Tickets_Items>  parseProducts (){

        JSONObject jsonObject ;

        ArrayList<Get_Tickets_Items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                Get_Tickets_Items products = new Get_Tickets_Items();

                JSONObject jo = productsJA.getJSONObject(i);

                products.setTicket_Id(jo.getString(KEY_ID));
                products.setTicket_Name(jo.getString(KEY_TITLE));
                products.setTicket_desc(jo.getString(KEY_desc));
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