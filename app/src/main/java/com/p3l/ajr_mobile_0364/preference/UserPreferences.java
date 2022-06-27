package com.p3l.ajr_mobile_0364.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.p3l.ajr_mobile_0364.model.User;

public class UserPreferences {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public static final String ID_LOGIN = "id";
    public static final String NAME_LOGIN = "name";
    public static final String TOKEN_LOGIN = "token" ;
    public static final String TOKEN_TYPE_LOGIN = "token_type" ;
    public static final String TOKEN_SCOPE_LOGIN = "token_scope" ;
    public static final String IS_LOGIN = "isLogin";

    public UserPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLogin(String id, String token, String token_type){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(ID_LOGIN, id);
        editor.putString(TOKEN_LOGIN, token);
        editor.putString(TOKEN_TYPE_LOGIN, token_type);
        editor.commit();
    }

    public void setLoginManager(String id, String name, String token, String token_scope, String token_type){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(ID_LOGIN, id);
        editor.putString(NAME_LOGIN, name);
        editor.putString(TOKEN_LOGIN, token);
        editor.putString(TOKEN_SCOPE_LOGIN, token_scope);
        editor.putString(TOKEN_TYPE_LOGIN, token_type);
        editor.commit();
    }

    public String getUserLoginId(){
        return sharedPreferences.getString(ID_LOGIN, null);
    }

    public String getNameLogin() {
        return sharedPreferences.getString(NAME_LOGIN, null);
    }

    public String getUserLoginTokenType(){
        return sharedPreferences.getString(TOKEN_TYPE_LOGIN, null);
    }

    public String getUserLoginToken(){
        return sharedPreferences.getString(TOKEN_LOGIN, null);
    }

    public String getUserLoginTokenScope(){
        return sharedPreferences.getString(TOKEN_SCOPE_LOGIN, null);
    }

    public boolean checkLogin(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }
}
