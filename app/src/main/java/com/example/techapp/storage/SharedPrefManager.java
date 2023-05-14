package com.example.techapp.storage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.techapp.activity.LoginActivity;
import com.example.techapp.model.User;

public class SharedPrefManager {

    // key user
    private static final String USER = "user";

    // key thuộc tính
    private static final String ID = "";
    private static final String FULLNAME = "fullname";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String AVATAR = "avatar";
    private static final String SDT = "sdt";


    private static SharedPrefManager instance;
    private static Context context;

    private SharedPrefManager(Context context){
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if (instance == null){
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void userLogin(User user){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(USER, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor =
                sharedPreferences.edit();
        editor.putInt(ID, user.getId());
        editor.putString(FULLNAME, user.getFullName());
        editor.putString(USERNAME, user.getUserName());
        editor.putString(PASSWORD, user.getPassword());
        editor.putString(EMAIL, user.getEmail());
        editor.putString(AVATAR, user.getAvatar());
        editor.putString(SDT, user.getSdt());
        editor.apply();
    }

    public User getUser(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        User user = new User();
        user.setId(sharedPreferences.getInt(ID, -1));
        user.setFullName(sharedPreferences.getString(FULLNAME, null));
        user.setUserName(sharedPreferences.getString(USERNAME, null));
        user.setPassword(sharedPreferences.getString(PASSWORD, null));
        user.setEmail(sharedPreferences.getString(EMAIL,null));
        user.setAvatar(sharedPreferences.getString(AVATAR, null));
        user.setSdt(sharedPreferences.getString(SDT, null));
        return user;
    }

    public void setAvatar(String avatar){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AVATAR, avatar);
        editor.apply();
    }
    public void logout(){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
//        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public boolean isLogined(){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        boolean isUsernameEmpty = sharedPreferences.getString(USERNAME, "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString(PASSWORD, "").isEmpty();
        return !(isUsernameEmpty || isPasswordEmpty);
    }
}
