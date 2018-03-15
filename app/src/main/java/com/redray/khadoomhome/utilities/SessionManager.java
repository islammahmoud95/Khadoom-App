package com.redray.khadoomhome.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


public class SessionManager {
    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Sharedpref file name
    private static final String PREF_NAME = "Toredo_APP";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    private Context context;


    public static final String KEY_USER_ID ="";
    public static final String KEY_USER_NAME ="";


    public static boolean isFirst(Context context){
        final SharedPreferences reader = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first", true);
        if(first){
            final SharedPreferences.Editor editor = reader.edit();
            editor.putBoolean("is_first", false);
            editor.apply();
        }
        return first;
    }

    // Constructor
    public SessionManager(Context context){

        this.context=context;
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }


//    public void checkLogin(){
//        // Check login status
//        if(!this.isLoggedIn()){
//            // user is not logged in redirect him to Login Activity
//            Intent i = new Intent(context, LoginActivity.class);
//            // Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            // Staring Login Activity
//            context.startActivity(i);
//        }
//
//    }

    /**
     * Create login session
     * */
    public void createLoginSession(String user_name,String Token, String pic,String email,String user_Type ,
                                   String user_id , String address ,String lat , String lng) {



        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString("USER_ID",user_id);

        editor.putString("USER_NAME",user_name);

        editor.putString("User_Pic",pic);

        editor.putString("Email",email);

        editor.putString("User_Type",user_Type);

        editor.putString("Address",address);

        editor.putString("Server_token",Token);


        editor.putString("LATITUDE",lat);

        editor.putString("LONGITUDE",lng);

        // commit changes
        editor.commit();


       // Log.e("fffffff" , pref.getInt("USER_ID", 0)+" : " +pref.getString("USER_NAME", null));

    }




    public void setCookie(String cookie) {
        pref.edit().putString("Cookie", cookie).apply();
    }

    public String getCookie() {
        return pref.getString("Cookie", "");
    }



    public String getUser_Type() {
        return pref.getString("User_Type",null);
    }

    public void setUser_Type(String userType ) {
        editor.putString("User_Type" , userType);
        // commit changes
        editor.commit();
    }


    public String getFirebase_Token() {
        return pref.getString("Firebase_TOKEN",null);
    }

    public void setFirebase_Token(String TOKEN ) {
        editor.putString("Firebase_TOKEN" , TOKEN);
        // commit changes
        editor.commit();
    }




    public String getLang() {
        return pref.getString("User_Lang","en");
    }

    public void set_user_lang(String Lang ) {
        editor.putString("User_Lang",Lang);
        // commit changes
        editor.commit();

    }
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();

        user.put("USER_ID", pref.getString("USER_ID", null));

        user.put("USER_NAME", pref.getString("USER_NAME", null));

        user.put("User_Pic", pref.getString("User_Pic", null));

        user.put("Email", pref.getString("Email", null));

        user.put("User_Type", pref.getString("User_Type", null));

        user.put("Server_token", pref.getString("Server_token", null));

        user.put("Address", pref.getString("Address", null));

        user.put("LATITUDE", pref.getString("LATITUDE", null));

        user.put("LONGITUDE", pref.getString("LONGITUDE", null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser() {
        // Clearing all data from Shared Preferences

     //   editor.clear();

        editor.putBoolean(IS_LOGIN, false);
        editor.remove("USER_ID");
        editor.remove("USER_NAME");
        editor.remove("User_Pic");
        editor.remove("Email");
        editor.remove("User_Type");
        editor.remove("Server_token");
        editor.remove("Address");
        editor.remove("LATITUDE");
        editor.remove("LONGITUDE");
        editor.commit();
    }




















    public HashMap<String, String> getMain_Search() {

        HashMap<String, String> Main_Search = new HashMap<>();

        Main_Search.put("MAIN_COUNTRY", pref.getString("MAIN_COUNTRY", null));
        Main_Search.put("MAIN_CITY", pref.getString("MAIN_CITY", null));

        return Main_Search;
    }



    public void setMain_search(String Country , String City ) {

        editor.putString("MAIN_COUNTRY",Country);

        editor.putString("MAIN_CITY",City);

        // commit changes
        editor.commit();

    }

}
