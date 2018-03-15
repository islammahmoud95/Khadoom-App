package com.redray.khadoomhome.tickets.parser;

import android.util.Log;

import com.redray.khadoomhome.tickets.models.Get_Details_Tickets_Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class ParseGet_Details_tickets {


    private static final String JSON_ARRAY ="replays";

    private static final String KEY_ID = "id";
    private static final String KEY_CONTENT= "content";
    private static final String KEY_IMAGE= "attach";
    private static final String KEY_SENDER ="sender";
    private static final String KEY_DATE= "created_at";


    private String json;

    public ParseGet_Details_tickets(String json)
    {
        this.json = json;
    }

    public ArrayList<Get_Details_Tickets_Items>  parseProducts (){

        JSONObject jsonObject ;

        ArrayList<Get_Details_Tickets_Items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                Get_Details_Tickets_Items products = new Get_Details_Tickets_Items();

                JSONObject jo = productsJA.getJSONObject(i);

                products.setReply_Id(jo.getString(KEY_ID));
                products.setReplyContent(jo.getString(KEY_CONTENT));
                products.setReplyImage(jo.getString(KEY_IMAGE));
                products.setReplyDate(jo.getString(KEY_DATE));
                products.setReplySender(jo.getString(KEY_SENDER));

                productsArrayList.add(products);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList",e.getMessage()+"");
        }

        return  productsArrayList;
    }

}