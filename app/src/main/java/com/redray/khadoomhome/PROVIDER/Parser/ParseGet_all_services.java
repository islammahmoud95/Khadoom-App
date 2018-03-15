package com.redray.khadoomhome.PROVIDER.Parser;

import android.util.Log;

import com.redray.khadoomhome.PROVIDER.Models.Get_all_services_Items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ParseGet_all_services {


    private static final String JSON_ARRAY = "Data";


    private static final String KEY_Main_SERV = "mainCat";
    private static final String KEY_SUB_SERV = "subCat";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CITIES = "city";


    private String json;

    public ParseGet_all_services(String json) {
        this.json = json;
    }

    public ArrayList<Get_all_services_Items> parseProducts() {

        JSONObject jsonObject ;

        ArrayList<Get_all_services_Items> productsArrayList = new ArrayList<>();


        try {
            jsonObject = new JSONObject(json);

            JSONArray productsJA = jsonObject.getJSONArray(JSON_ARRAY);


            for (int i = 0; i < productsJA.length(); i++) {
                Get_all_services_Items products = new Get_all_services_Items();

                JSONObject jo = productsJA.getJSONObject(i);

                //   products.setId_SERV(jo.getString(KEY_ID));


                StringBuilder sub_str = new StringBuilder();
                String street ;
                JSONArray key_sub = jo.getJSONArray(KEY_SUB_SERV);
                for (int sub = 0; sub < key_sub.length(); sub++) {
                    street = key_sub.getString(sub);
                    sub_str.append(street);

                    if (key_sub.length() -1 != sub) {
                        sub_str.append(" - ");
                    }
                    Log.i("teeest", sub_str.toString());
                    // loop and add it to array or arraylist
                }

                products.setSub_SERV(sub_str.toString());


                StringBuilder main_str = new StringBuilder();
                String street2 ;
                JSONArray key_main = jo.getJSONArray(KEY_Main_SERV);
                for (int sub = 0; sub < key_main.length(); sub++) {
                    street2 = key_main.getString(sub);
                    main_str.append(street2);

                    if (key_main.length() -1 != sub) {
                        main_str.append(" - ");
                    }

                    Log.i("teeest", main_str.toString());
                    // loop and add it to array or arraylist
                }


                products.setMain_SERV(main_str.toString());


                products.setCountry_SERV(jo.getString(KEY_COUNTRY));
                products.setCities_SERV(jo.getString(KEY_CITIES));


                productsArrayList.add(products);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("productsArrayList", e.getMessage() + "");
        }

        return productsArrayList;
    }

}