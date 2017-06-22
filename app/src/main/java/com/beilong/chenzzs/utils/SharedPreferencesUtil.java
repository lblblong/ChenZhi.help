package com.beilong.chenzzs.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.beilong.chenzzs.app.AppApplication;

/**
 * Created by LBL on 2016/11/7.
 * 系统设置相关SharedPreferences
 */

public class SharedPreferencesUtil {

    public static final String CURRENT_CLASS = "CurrentClass"; //当前班级
    public static final String CURRENT_VERSION = "CurrentVersion";  //当前版本
    public static final String ZBL = "zbl";

    private static SharedPreferencesUtil instance;
    private SharedPreferences sp;

    private SharedPreferencesUtil(){
        sp = AppApplication.getAppContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    public static SharedPreferencesUtil getInstance(){
        if(instance == null){
            instance = new SharedPreferencesUtil();
        }
        return instance;
    }

    //-------------------------版本---------------------------
    public void changeCurrentVersion(String version){
        sp.edit().putString(CURRENT_VERSION,version).apply();
    }

    public String getCurrentVersion(String defValue){
        return sp.getString(CURRENT_VERSION,defValue);
    }

    //------------------------当前班级--------------------------
    public void changeCurrentClass(String classname){
        sp.edit().putString(CURRENT_CLASS,classname).apply();
    }

    public String getCurrentClass(){
        return sp.getString(CURRENT_CLASS,null);
    }

    //----------------------------章冰丽------------------------
    public void changeZBL(boolean b){
        sp.edit().putBoolean(ZBL,b).apply();
    }

    public boolean getZBL(){
        return sp.getBoolean(ZBL,true);
    }
}
