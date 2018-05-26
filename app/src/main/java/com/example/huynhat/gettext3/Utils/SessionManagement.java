package com.example.huynhat.gettext3.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.huynhat.gettext3.Account.LoginActivity;

import java.util.HashMap;

/**
 * Created by 300239275 on 11/15/2017.
 * Preference: techobbist.com/
 */

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;


    private static final String PREF_NAME ="GetTextAppPrefs"; //file name
    //private static final String LOGGED_IN_MODE ="isLoggedIn";


    public SessionManagement(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)  ;
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String email){
        editor.putBoolean("isLoggedIn", true);
        editor.putString("email", email);
        editor.putBoolean("loadDatabase", true);
        editor.commit();
    }

    public String getUserEmail(){
        return sharedPreferences.getString("email","");
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    public boolean datbaseLoad(){
        return sharedPreferences.getBoolean("loadDatabase", false);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(context,LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }




    /*
    //Login state: true | false
    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    //Login Session
    public void createLoginSession(String name, String email){
        //Set isLoggedIn == true
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);

        editor.commit();
    }

    //Check login or not : NOT --> Login Page
    public void checkLogin(){
        if(this.isLoggedIn() == false){ //directing to Login Activity
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//Closing all actvities
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user =
    }
    */

}
