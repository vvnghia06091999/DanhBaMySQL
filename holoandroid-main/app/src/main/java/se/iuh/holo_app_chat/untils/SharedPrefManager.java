package se.iuh.holo_app_chat.untils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SP_LOGIN_APP = "spLoginApp";

    public static final String SP_NAME = "spName";
    public static final String SP_USER_ID = "spUserId";
    public static final String SP_PHOTO_URL = "spPhotoURL";
    public static final String SP_TOKEN = "spToken";
    public static final String SP_REFRESH_TOKEN = "spRefreshToken";
    public static final String SP_LOGIN = "spLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;


    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_LOGIN_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPName(){
        return sp.getString(SP_NAME, "");
    }

    public String getSpPhotoUrl(){
        return sp.getString(SP_PHOTO_URL, "");
    }

    public int getUserId(){
        return sp.getInt(SP_USER_ID, 0);
    }

    public String getSPToken(){
        return sp.getString(SP_TOKEN, "");
    }

    public Boolean getSPSLogin(){
        return sp.getBoolean(SP_LOGIN, false);
    }

    public String getSpRefreshToken() {
        return sp.getString(SP_REFRESH_TOKEN, "");
    }
}
