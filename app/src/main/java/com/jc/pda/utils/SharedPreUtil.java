package com.jc.pda.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by z on 2017/12/22.
 */

public class SharedPreUtil {
    private static final String LOGIN = "login";
    private static final String LOGIN_USER = "user";
    private static final String LOGIN_PWD = "pwd";
    private static final String LOGIN_ISSAVEPWD = "isSave";
    private static final String LOGIN_ISAUTOLOGIN = "isAutoLogin";


    private static final String USER = "user";
    private static final String USER_CID = "CID";

    private static final String SET = "set";
    /**
     * 提醒安装ISACAN
     * ture 提醒
     * false 不提醒
     */
    private static final String SET_INSTALLISCAN = "installiscan";
    /**
     * 出库经销商筛选关键字
     */
    private static final String SET_OUT_DEALERS_KEYWORD = "out_dealers_keyword";


    public static void saveUserCID(Context context , String CID){
        SharedPreferences sharedUser = context.getSharedPreferences(USER,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedUser.edit();
        editor.putString(USER_CID,CID);
        editor.commit();
    }

    public static String getUserCID(Context context){
        SharedPreferences sharedUser = context.getSharedPreferences(USER,Context.MODE_PRIVATE);
        return sharedUser.getString(USER_CID,"");
    }

    public static void saveLogin(Context context ,String user,String pwd,boolean isSave,boolean isAuto){
        SharedPreferences sharedUser = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedUser.edit();
        editor.putString(LOGIN_USER,user);
        if (isSave){
            editor.putString(LOGIN_PWD,pwd);
        }else {
            editor.putString(LOGIN_PWD,"");
        }
        editor.putBoolean(LOGIN_ISSAVEPWD,isSave);
        editor.putBoolean(LOGIN_ISAUTOLOGIN,isAuto);
        editor.commit();
    }

    public static String getLoginUser(Context context){
        SharedPreferences sharedUser = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        return sharedUser.getString(LOGIN_USER,"");
    }

    public static String getLoginPwd(Context context){
        SharedPreferences sharedUser = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        return sharedUser.getString(LOGIN_PWD,"");
    }

    public static boolean getIsSave(Context context){
        SharedPreferences sharedUser = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        return sharedUser.getBoolean(LOGIN_ISSAVEPWD,false);
    }

    public static boolean getIsAuto(Context context){
        SharedPreferences sharedUser = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        return sharedUser.getBoolean(LOGIN_ISAUTOLOGIN,false);
    }

    public static void clearLogin(Context context){
        SharedPreferences sharedUser = context.getSharedPreferences(LOGIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedUser.edit();
        editor.putString(LOGIN_PWD,"");
        editor.putBoolean(LOGIN_ISAUTOLOGIN,false);
        editor.commit();
    }


    public static void saveSetIntallIsan(Context context , boolean intall){
        SharedPreferences sharedUser = context.getSharedPreferences(SET,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedUser.edit();
        editor.putBoolean(SET_INSTALLISCAN,intall);
        editor.commit();
    }

    public static boolean getSetIntallIsan(Context context){
        SharedPreferences sharedUser = context.getSharedPreferences(SET,Context.MODE_PRIVATE);
        return sharedUser.getBoolean(SET_INSTALLISCAN,true);
    }

    public static void saveOutDealerKeyword(Context context , String keyword){
        SharedPreferences sharedUser = context.getSharedPreferences(SET,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedUser.edit();
        editor.putString(SET_OUT_DEALERS_KEYWORD,keyword.trim());
        editor.commit();
    }

    public static String getOutDealerKeyword(Context context){
        SharedPreferences sharedUser = context.getSharedPreferences(SET,Context.MODE_PRIVATE);
        return sharedUser.getString(SET_OUT_DEALERS_KEYWORD,null);
    }

}
