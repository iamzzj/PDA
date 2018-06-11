package com.jc.pda.utils;

import android.text.TextUtils;

/**
 * Created by z on 2018/1/3.
 */

public class TextUtil {
    /**
     * 过滤条码中的其他信息
     * @param barcode
     * @return
     */
    public static String filterCode(String barcode){
        if(TextUtils.isEmpty(barcode)){
            return "";
        }
        if (barcode.contains("=")) {
            barcode = barcode.substring(
                    barcode.indexOf("=") + 1,barcode.length());
        }
        if (barcode.contains("/")) {
            String[] ss = barcode.split("/");
            if(ss.length > 0){
                barcode = ss[ss.length-1];
            }
        }

        if(barcode.startsWith("531")){
            if(barcode.length()>14){
                barcode = barcode.substring(0, 14);
            }
        }else{
            if(barcode.length()>20){
                barcode = barcode.substring(0, 20);
            }
        }
        return barcode;
    }
}
