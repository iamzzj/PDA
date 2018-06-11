package com.jc.pda.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by z on 2017/12/25.
 */

public class PermissionUtils {

    /**
     * 申请某个权限
     * @param context
     * @param requestCode
     * @param permission
     */
    public static void requestPermission(final Activity context, int requestCode, String permission) {
        if (context == null) {
            return ;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return ;
        }

        int checkSelfPermission =  ActivityCompat.checkSelfPermission(context, permission);

        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            //如果未开启，则判断是否需要向用户解释为何申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {

            } else {
                //申请权限
                ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
            }
        }

    }

    /**
     * 检测某个权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkSelfPermission(Context context,String permission){
        if (context == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        int checkSelfPermission =  ActivityCompat.checkSelfPermission(context, permission);

        if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    //一次申请多个
    public static void requestDataBasePermission(final Activity activity,int requestCode) {
        if (activity == null) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.READ_PHONE_STATE
        };
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 权限返回结果
     * @param grantResults
     * @return
     */
    public static boolean getPermissionResult(int[] grantResults){
        for(int result : grantResults){
            if(PackageManager.PERMISSION_GRANTED != result)
                return false;
        }
        return true;
    }

}
