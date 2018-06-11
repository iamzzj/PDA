package com.jc.pda.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jc.pda.database.date.PdaDataBase;
import com.jc.pda.database.table.DealerTable;
import com.jc.pda.entity.Dealer;
import com.jc.pda.entity.UpDate;
import com.jc.pda.entitynet.DealerNet;
import com.jc.pda.utils.TimeUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2017/12/21.
 */

public class DealerHelper {
    private SQLiteDatabase db;

    private DealerHelper(Context context){
        db = PdaDataBase.getSingleton(context).getWritableDatabase();;
    }

    private volatile static DealerHelper singleton;

    public static DealerHelper getSingleton(Context context) {
        if (singleton == null) {
            synchronized (DealerHelper.class) {
                if (singleton == null) {
                    singleton = new DealerHelper(context);
                }
            }
        }
        return singleton;
    }

    public void insertDealers(List<DealerNet> dealers){
        if(dealers!=null&&dealers.size()>0){
            ContentValues values = new ContentValues();
            for(DealerNet dealer:dealers){
                UpDate upDate = new UpDate();
                values.put(DealerTable.DealerId,dealer.getCID());
                values.put(DealerTable.DealerNo,dealer.getCGuestCode());
                values.put(DealerTable.DealerName,dealer.getCGuestName());
                values.put(DealerTable.IsDelete,String.valueOf(false));
                values.put(DealerTable.UpDateInt,upDate.getTime());
                values.put(DealerTable.UpDateString, upDate.getUpDate());
                db.insert(DealerTable.TABLE,null,values);
                values.clear();
            }
        }
    }

    public void deleteAll(){
        db.delete(DealerTable.TABLE,null,null);
    }

    public List<Dealer> getDealers(){
        List<Dealer> dealers = new ArrayList<>();

        String[] columns = {DealerTable.DealerNo,DealerTable.DealerName};
        Cursor cursor = db.query(false,DealerTable.TABLE,columns,null,null,null,null,null,null);

        if(cursor.getCount() > 0){
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
                Dealer dealer = new Dealer();
                dealer.setDealerNo(cursor.getString(cursor.getColumnIndex(DealerTable.DealerNo)));
                dealer.setDealerName(cursor.getString(cursor.getColumnIndex(DealerTable.DealerName)));
                dealers.add(dealer);
            }
        }
        cursor.close();

        return dealers;
    }

    public List<Dealer> getDealersByKey(String keyword){
        Logger.i(keyword);
        List<Dealer> dealers = new ArrayList<>();
        if(TextUtils.isEmpty(keyword)) {
            String[] columns = {DealerTable.DealerNo, DealerTable.DealerName};
            Cursor cursor = db.query(false, DealerTable.TABLE, columns, null, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Dealer dealer = new Dealer();
                    dealer.setDealerNo(cursor.getString(cursor.getColumnIndex(DealerTable.DealerNo)));
                    dealer.setDealerName(cursor.getString(cursor.getColumnIndex(DealerTable.DealerName)));
                    dealers.add(dealer);
                }
            }
            cursor.close();
        }else{
            String[] columns = {DealerTable.DealerNo, DealerTable.DealerName};
            String selection = DealerTable.DealerName +" like ? ";
            String[] selectionArgs = {"%"+keyword+"%"};
            Cursor cursor = db.query(false, DealerTable.TABLE, columns, selection, selectionArgs, null, null, null, null);

            if (cursor.getCount() > 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    Dealer dealer = new Dealer();
                    dealer.setDealerNo(cursor.getString(cursor.getColumnIndex(DealerTable.DealerNo)));
                    dealer.setDealerName(cursor.getString(cursor.getColumnIndex(DealerTable.DealerName)));
                    dealers.add(dealer);
                }
            }
            cursor.close();
        }

        return dealers;
    }


    public String getDealerName(String dealerId){
        String dealerName = "";

        if(!TextUtils.isEmpty(dealerId)) {
            String[] columns = {DealerTable.DealerName};
            String selection = DealerTable.DealerNo + " = ? ";
            String[] selectionArgs = { dealerId };
            Cursor cursor = db.query(false, DealerTable.TABLE, columns, selection, selectionArgs, null, null, null, null);

            if (cursor.getCount() > 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    dealerName = cursor.getString(cursor.getColumnIndex(DealerTable.DealerName));
                }
            }
            cursor.close();
        }

        return dealerName;
    }

    public int getCount(){
        String[] columns = {"count(*)"};
        Cursor cursor = db.query(false,DealerTable.TABLE,columns,null,null,null,null,null,null);

        int count = 0;

        if(cursor.getCount()>0){
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                count = cursor.getInt(cursor.getColumnIndex("count(*)"));
            }
        }
        cursor.close();

        return count;
    }

    public String getDealerNameById(String dealerId){
        String dealerName = "未找到经销商名字，资料变动。经销商ID:"+dealerId;

        if(!TextUtils.isEmpty(dealerId)) {
            String selection = DealerTable.DealerNo + " = ?";
            String[] selectionArgs = {dealerId};
            String[] columns = {DealerTable.DealerName};
            Cursor cursor = db.query(true, DealerTable.TABLE, columns, selection, selectionArgs, null, null, null, null);

            if (cursor.getCount() > 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    dealerName = cursor.getString(cursor.getColumnIndex(DealerTable.DealerName));
                }
            }
            cursor.close();
        }

        return dealerName;
    }
}
