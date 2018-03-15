package com.redray.khadoomhome.PROVIDER.Parser;

import android.util.Log;

import com.redray.khadoomhome.PROVIDER.Models.Get_ticknicals_Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;




public class ParseGet_Ticknicals {


    private static final String JSON_ARRAY ="Data";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME= "techName";
    private static final String KEY_NUMBER= "phone_num";
    private static final String KEY_SECTION= "section_name";
    private static final String KEY_IMG= "profile_img";
    private static final String KEY_ISACTIVATE= "isActive";


    private String json;
    public ParseGet_Ticknicals(String json)
    {
        this.json = json;
    }
    public ArrayList<Get_ticknicals_Items>  parseProducts (){

        JSONObject jsonObject ;

        ArrayList<Get_ticknicals_Items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                Get_ticknicals_Items products = new Get_ticknicals_Items();

                JSONObject jo = productsJA.getJSONObject(i);

                products.setTeck_Id(jo.getString(KEY_ID));
                products.setTeck_Name(jo.getString(KEY_NAME));
                products.setTeck_Img(jo.getString(KEY_IMG));
                products.setTeck_section(jo.getString(KEY_SECTION));
                products.setTeck_Number(jo.getString(KEY_NUMBER));
                products.setTeck_Status(jo.getString(KEY_ISACTIVATE));

                productsArrayList.add(products);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList",e.getMessage()+"");
        }

        return  productsArrayList;
    }

}