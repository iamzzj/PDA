package com.jc.pda.utils;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

/**
 * Created by z on 2017/12/21.
 */

public class Path {

    public static String getDataBasePath(Context context){
        String dirPath = "";
        //小于6.0
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                dirPath = Environment.getExternalStorageDirectory().getPath() + "/" + Global.NAME + "DB";
                Logger.i("SDCARD");
            }
        }else{
            // 大于等于6.0
            Logger.i(PermissionUtils.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)+"--"
                    +PermissionUtils.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE));
            //有读写权限
            if (PermissionUtils.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    && PermissionUtils.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    dirPath = Environment.getExternalStorageDirectory().getPath() + "/" + Global.NAME + "DB";

                    Logger.i("SDCARD");
                }
            }
        }
        if(TextUtils.isEmpty(dirPath)){
            Logger.i("Data");
        }
        return  dirPath;
    }

    public static String getDownAPK(Context context){
        String dirPath = context.getCacheDir().getPath();
        //小于6.0
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                dirPath = Environment.getExternalStorageDirectory().getPath() + "/" + Global.NAME + "APK";
                Logger.i("SDCARD");
            }
        }else{
            // 大于等于6.0
            Logger.i(PermissionUtils.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)+"--"
                    +PermissionUtils.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE));
            //有读写权限
            if (PermissionUtils.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    && PermissionUtils.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    dirPath = Environment.getExternalStorageDirectory().getPath() + "/" + Global.NAME + "APK";

                    Logger.i("SDCARD");
                }
            }
        }
        if(TextUtils.isEmpty(dirPath)){
            Logger.i("Data");
        }

        dirPath = dirPath + "/"+Global.NAME+".apk";
        return  dirPath;
    }
}
