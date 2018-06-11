package com.jc.pda.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by z on 2018/1/2.
 */

public class UpDate {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private long time;
    private String upDate;
    private String upDateDetail;

    public UpDate() {
        long currentTime = System.currentTimeMillis();

        setTime(currentTime);

        Date date = new Date(currentTime);
        setUpDate(sdf.format(date));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DATE);
        int H = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        int s = calendar.get(Calendar.SECOND);
        int ms = calendar.get(Calendar.MILLISECOND);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(year);
        if(month<10){
            stringBuilder.append("0");
        }
        stringBuilder.append(month);
        if(day<10){
            stringBuilder.append("0");
        }
        stringBuilder.append(day);
        if(H<10){
            stringBuilder.append("0");
        }
        stringBuilder.append(H);
        if(m<10){
            stringBuilder.append("0");
        }
        stringBuilder.append(m);
        if(s<10){
            stringBuilder.append("0");
        }
        stringBuilder.append(s);
        if(ms<10){
            stringBuilder.append("00");
        }else if(ms<100){
            stringBuilder.append("0");
        }
        stringBuilder.append(ms);

        setUpDateDetail(stringBuilder.toString());
    }

    public long getTime() {
        return time;
    }

    private void setTime(long time) {
        this.time = time;
    }

    public String getUpDate() {
        return upDate;
    }

    private void setUpDate(String upDate) {
        this.upDate = upDate;
    }

    public String getUpDateDetail() {
        return upDateDetail;
    }

    private void setUpDateDetail(String upDateDetail) {
        this.upDateDetail = upDateDetail;
    }

    @Override
    public String toString() {
        return "UpDate{" +
                "time=" + time +
                ", upDate='" + upDate + '\'' +
                ", upDateDetail='" + upDateDetail + '\'' +
                '}';
    }
}
