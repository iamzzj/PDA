package com.jc.pda.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jc.pda.database.date.PdaDataBase;
import com.jc.pda.database.table.BillTable;
import com.jc.pda.entity.Bill;
import com.jc.pda.utils.TextUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2018/1/2.
 */

public class BillHelper {
    private SQLiteDatabase db;

    private BillHelper(Context context){
        db = PdaDataBase.getSingleton(context).getWritableDatabase();
    }

    private volatile static BillHelper singleton;
    public static BillHelper getSingleton(Context context) {
        if (singleton == null) {
            synchronized (BillHelper.class) {
                if (singleton == null) {
                    singleton = new BillHelper(context);
                }
            }
        }
        return singleton;
    }

    public void insertBill(Bill bill){
        if(bill != null){
            ContentValues values = new ContentValues();
            values.put(BillTable.BillId,bill.getBillId());
            values.put(BillTable.GoodId,bill.getProductId());
            values.put(BillTable.BatchId,bill.getBatchId());
            values.put(BillTable.WholesaleId,bill.getWholesaleId());
            values.put(BillTable.DealerId,bill.getDealerId());
            values.put(BillTable.ShopId,bill.getShopId());
            values.put(BillTable.BillStyle,bill.getBillStyle());
            values.put(BillTable.UpType,bill.getUpType());
            values.put(BillTable.isUp,String.valueOf(bill.isUp()));
            values.put(BillTable.OldBillNo,bill.getOldBillNo());
            values.put(BillTable.AdminId,bill.getAdminId());
            values.put(BillTable.UpDateString,bill.getUpDateString());
            values.put(BillTable.UpDateInt,bill.getUpDateInt());
            values.put(BillTable.IsDelete,String.valueOf(bill.isDelete()));

            db.insert(BillTable.TABLE,null,values);

            values.clear();
        }
    }

    public List<Bill> getBills(int billStyle,String UpDateString,String productId,String batchId){
        List<Bill> bills = new ArrayList<>();

        Cursor cursor = null;
        if(!TextUtils.isEmpty(productId)&&!TextUtils.isEmpty(batchId)){
            String selection = BillTable.BillStyle +" = ? and " +BillTable.UpDateString + " = ? and "+BillTable.GoodId +" = ? and "+BillTable.BatchId +" = ? ";
            String[] selectionArgs = {String.valueOf(billStyle),UpDateString,productId,batchId};
            String[] columns = {"*"};
            String orderBy = BillTable.UpDateInt + " desc";
            cursor = db.query(false,BillTable.TABLE,columns,selection,selectionArgs,null,null,orderBy,null);
        }else{
            String selection = BillTable.BillStyle +" = ? and " +BillTable.UpDateString + " = ? ";
            String[] selectionArgs = {String.valueOf(billStyle),UpDateString};
            String[] columns = {"*"};
            String orderBy = BillTable.UpDateInt + " desc";
            cursor = db.query(false,BillTable.TABLE,columns,selection,selectionArgs,null,null,orderBy,null);
        }

        if(cursor.getCount()>0){
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
                Bill bill = new Bill();
                bill.setBillId(cursor.getString(cursor.getColumnIndex(BillTable.BillId)));
                bill.setProductId(cursor.getString(cursor.getColumnIndex(BillTable.GoodId)));
                bill.setBatchId(cursor.getString(cursor.getColumnIndex(BillTable.BatchId)));
                bill.setWholesaleId(cursor.getString(cursor.getColumnIndex(BillTable.WholesaleId)));
                bill.setDealerId(cursor.getString(cursor.getColumnIndex(BillTable.DealerId)));
                bill.setShopId(cursor.getString(cursor.getColumnIndex(BillTable.ShopId)));
                bill.setBillStyle(cursor.getInt(cursor.getColumnIndex(BillTable.BillStyle)));
                bill.setUpType(cursor.getInt(cursor.getColumnIndex(BillTable.UpType)));
                bill.setUp(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(BillTable.isUp))));
                bill.setOldBillNo(cursor.getString(cursor.getColumnIndex(BillTable.OldBillNo)));
                bill.setAdminId(cursor.getString(cursor.getColumnIndex(BillTable.AdminId)));
                bill.setUpDateString(cursor.getString(cursor.getColumnIndex(BillTable.UpDateString)));
                bill.setUpDateInt(cursor.getLong(cursor.getColumnIndex(BillTable.UpDateInt)));
                bill.setDelete(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(BillTable.IsDelete))));
                bills.add(bill);
            }
        }
        cursor.close();

        return bills;
    }

    public List<Bill> getBills(int billStyle,String UpDateString,String dealerId){
        List<Bill> bills = new ArrayList<>();

        Cursor cursor = null;
        if(!TextUtils.isEmpty(dealerId)){
            String selection = BillTable.BillStyle +" = ? and " +BillTable.UpDateString + " = ? and "+BillTable.DealerId +" = ? ";
            String[] selectionArgs = {String.valueOf(billStyle),UpDateString,dealerId};
            String[] columns = {"*"};
            cursor = db.query(false,BillTable.TABLE,columns,selection,selectionArgs,null,null,null,null);
        }else{
            String selection = BillTable.BillStyle +" = ? and " +BillTable.UpDateString + " = ? ";
            String[] selectionArgs = {String.valueOf(billStyle),UpDateString};
            String[] columns = {"*"};
            cursor = db.query(false,BillTable.TABLE,columns,selection,selectionArgs,null,null,null,null);
        }

        if(cursor.getCount()>0){
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
                Bill bill = new Bill();
                bill.setBillId(cursor.getString(cursor.getColumnIndex(BillTable.BillId)));
                bill.setProductId(cursor.getString(cursor.getColumnIndex(BillTable.GoodId)));
                bill.setBatchId(cursor.getString(cursor.getColumnIndex(BillTable.BatchId)));
                bill.setWholesaleId(cursor.getString(cursor.getColumnIndex(BillTable.WholesaleId)));
                bill.setDealerId(cursor.getString(cursor.getColumnIndex(BillTable.DealerId)));
                bill.setShopId(cursor.getString(cursor.getColumnIndex(BillTable.ShopId)));
                bill.setBillStyle(cursor.getInt(cursor.getColumnIndex(BillTable.BillStyle)));
                bill.setUpType(cursor.getInt(cursor.getColumnIndex(BillTable.UpType)));
                bill.setUp(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(BillTable.isUp))));
                bill.setOldBillNo(cursor.getString(cursor.getColumnIndex(BillTable.OldBillNo)));
                bill.setAdminId(cursor.getString(cursor.getColumnIndex(BillTable.AdminId)));
                bill.setUpDateString(cursor.getString(cursor.getColumnIndex(BillTable.UpDateString)));
                bill.setUpDateInt(cursor.getLong(cursor.getColumnIndex(BillTable.UpDateInt)));
                bill.setDelete(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(BillTable.IsDelete))));
                bills.add(bill);
            }
        }
        cursor.close();

        return bills;
    }

    /**
     * 获取订单数量
     * @param upDateString 日期
     * @param billStyle 0 1 2 3 入 出 退 出入
     * @param upType 0 1 首次 重复
     * @param isUp 是否上传
     * @return
     */
    public int getBillCount(String upDateString,int billStyle,int upType,boolean isUp){
        int count = 0;

        String selection = BillTable.UpDateString +" = ? and "+BillTable.BillStyle +" = ? and "+BillTable.UpType +" = ? and "+BillTable.isUp +" = ? ";
        String[] selectionArgs = {upDateString,String.valueOf(billStyle),String.valueOf(upType),String.valueOf(isUp)};
        String[] columns = {"count(*)"};
        Cursor cursor = db.query(false,BillTable.TABLE,columns,selection,selectionArgs,null,null,null,null);

        if(cursor.getCount()>0){
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                count = cursor.getInt(cursor.getColumnIndex("count(*)"));
            }
        }
        cursor.close();

        return count;
    }

    /**
     * 获取订单数量
     * @param upYearString 日期
     * @param billStyle 0 1 2 3 入 出 退 出入
     * @return
     */
    public int getYearMonthBillCount(String upYearString,int billStyle,int upType,boolean isUp){
        int count = 0;
        if(upType>=0){
            String selection = BillTable.UpDateString +" like ? and "+BillTable.BillStyle +" = ? and "+BillTable.UpType +" = ? and "+BillTable.isUp +" = ? ";
            String[] selectionArgs = {upYearString+"%",String.valueOf(billStyle),String.valueOf(upType),String.valueOf(isUp)};
            String[] columns = {"count(*)"};
            Cursor cursor = db.query(false,BillTable.TABLE,columns,selection,selectionArgs,null,null,null,null);

            if(cursor.getCount()>0){
                for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                    count = cursor.getInt(cursor.getColumnIndex("count(*)"));
                }
            }
            cursor.close();
        }else{
            String selection = BillTable.UpDateString +" like ? and "+BillTable.BillStyle +" = ? ";
            String[] selectionArgs = {upYearString+"%",String.valueOf(billStyle)};
            String[] columns = {"count(*)"};
            Cursor cursor = db.query(false,BillTable.TABLE,columns,selection,selectionArgs,null,null,null,null);

            if(cursor.getCount()>0){
                for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                    count = cursor.getInt(cursor.getColumnIndex("count(*)"));
                }
            }
            cursor.close();
        }


        return count;
    }


    public Bill getBillByBillId(String billId) {
        Bill bill = new Bill();

        String selection = BillTable.BillId + " = ? ";
        String[] selectionArgs = {billId};
        String[] columns = {"*"};
        Cursor cursor = db.query(true, BillTable.TABLE, columns, selection, selectionArgs, null, null, null, null);

        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                bill.setBillId(cursor.getString(cursor.getColumnIndex(BillTable.BillId)));
                bill.setProductId(cursor.getString(cursor.getColumnIndex(BillTable.GoodId)));
                bill.setBatchId(cursor.getString(cursor.getColumnIndex(BillTable.BatchId)));
                bill.setWholesaleId(cursor.getString(cursor.getColumnIndex(BillTable.WholesaleId)));
                bill.setDealerId(cursor.getString(cursor.getColumnIndex(BillTable.DealerId)));
                bill.setShopId(cursor.getString(cursor.getColumnIndex(BillTable.ShopId)));
                bill.setBillStyle(cursor.getInt(cursor.getColumnIndex(BillTable.BillStyle)));
                bill.setUpType(cursor.getInt(cursor.getColumnIndex(BillTable.UpType)));
                bill.setUp(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(BillTable.isUp))));
                bill.setOldBillNo(cursor.getString(cursor.getColumnIndex(BillTable.OldBillNo)));
                bill.setAdminId(cursor.getString(cursor.getColumnIndex(BillTable.AdminId)));
                bill.setUpDateString(cursor.getString(cursor.getColumnIndex(BillTable.UpDateString)));
                bill.setUpDateInt(cursor.getLong(cursor.getColumnIndex(BillTable.UpDateInt)));
                bill.setDelete(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(BillTable.IsDelete))));
            }
        }
        cursor.close();

        return bill;
    }

    public void upDataBillUP(Bill bill){
        if(bill!=null){
            String whereClause = BillTable.BillId + " = ? ";
            String[] whereArgs = {bill.getBillId()};
            ContentValues values = new ContentValues();
            values.put(BillTable.isUp,String.valueOf(bill.isUp()));

            db.update(BillTable.TABLE,values,whereClause,whereArgs);

            values.clear();
        }
    }
}
