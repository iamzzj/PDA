package com.jc.pda.utils;

import com.jc.pda.entity.UpDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by z on 2017/12/25.
 */

public class TimeUtils {
    private static final Random random = new Random();
    //private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   // private static final SimpleDateFormat sdfM = new SimpleDateFormat("MM-dd");

    public enum BillTye { RUKU,CHUKU,TUIHUO,CHURUKU};

    /**
     *
     * @param type
     * @param isFirst
     * @param isY 是否验证过
     * @return
     */
    public static String createBillNo (BillTye type , boolean isFirst,boolean isY){
        UpDate upDate = new UpDate();
        StringBuilder sb = new StringBuilder();
        if(!isFirst){
            sb.append("re");
        }else{
            if(!isY){
                sb.append("n");
            }
        }
        switch (type){
            case RUKU:
                sb.append("rk");
                break;
            case CHUKU:
                sb.append("ck");
                break;
            case TUIHUO:
                sb.append("th");
                break;
            case CHURUKU:
                sb.append("crk");
                break;
        }

        sb.append(upDate.getUpDateDetail());

        int ran = random.nextInt(9999);
        if(ran < 10){
            sb.append("000");
        }else if(ran < 100){
            sb.append("00");
        }else if(ran < 1000){
            sb.append("0");
        }
        sb.append(ran);

        return sb.toString();
    }

    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String formatDateYear(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    public static Date parseDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(date);
    }

    public static String getYesterday(String date , int i){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cYesterday = Calendar.getInstance();
        Date dy = new Date();
        try {
            dy = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cYesterday.setTime(dy);
        cYesterday.add(Calendar.DATE,-i);
        String yesterday = sdf.format(cYesterday.getTime());
        return yesterday;
    }

    public static String getYearDate(String date , int i){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date);
        stringBuilder.append("-");
        if(i<10){
            stringBuilder.append("0");
        }
        stringBuilder.append(i);
        return stringBuilder.toString();
    }

    /**
     * 将2000-00-00 编程00-00
     * @return
     */
    public static String changeDate(String datey){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfM = new SimpleDateFormat("MM-dd");
            Date dy = sdf.parse(datey);
            String d = sdfM.format(dy);

            return d;
        } catch (ParseException e) {
            e.printStackTrace();

            return "";
        }
    }

    /**
     * 将2000-00-00 编程00-00
     * @return
     */
    public static String changeDateY2M(String datey){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            SimpleDateFormat sdfM = new SimpleDateFormat("MM");
            Date dy = sdf.parse(datey);
            String d = sdfM.format(dy);

            return d;
        } catch (ParseException e) {
            e.printStackTrace();

            return "";
        }
    }

    public static String formatLong(long d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(d);
        return sdf.format(date);
    }
}
