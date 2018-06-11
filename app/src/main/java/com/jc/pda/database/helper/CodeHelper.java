package com.jc.pda.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jc.pda.database.date.PdaDataBase;
import com.jc.pda.database.table.CodeTable;
import com.jc.pda.entity.Code;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2018/1/3.
 */

public class CodeHelper {
    private SQLiteDatabase db;

    private CodeHelper(Context context){
        db = PdaDataBase.getSingleton(context).getWritableDatabase();
    }

    private volatile static CodeHelper singleton;
    public static CodeHelper getSingleton(Context context) {
        if (singleton == null) {
            synchronized (CodeHelper.class) {
                if (singleton == null) {
                    singleton = new CodeHelper(context);
                }
            }
        }
        return singleton;
    }

    public void insertCodes(List<Code> codes){
        if(codes != null&& codes.size()>0){
            ContentValues values = new ContentValues();
            for (Code code:codes){
                values.put(CodeTable.Code,code.getCode());
                values.put(CodeTable.GoodId,code.getProductId());
                values.put(CodeTable.BatchId,code.getBatchId());
                values.put(CodeTable.WholesaleId,code.getWholesaleId());
                values.put(CodeTable.DealerId,code.getDealerId());
                values.put(CodeTable.ShopId,code.getShopId());
                values.put(CodeTable.CodeStyle,code.getCodeStyle());
                values.put(CodeTable.BillId,code.getBillId());
                values.put(CodeTable.UpType,code.getUpType());
                values.put(CodeTable.AdminId,code.getAdminId());
                values.put(CodeTable.UpDateString,code.getUpDateString());
                values.put(CodeTable.UpDateInt,code.getUpDateInt());
                values.put(CodeTable.IsDelete,String.valueOf(code.isDelete()));

                db.insert(CodeTable.TABLE,null,values);
                values.clear();
            }
        }
    }

    public List<Code> getCodesByBillId(String billId){
        List<Code> codes = new ArrayList<>();

        String selection = CodeTable.BillId +" = ?";
        String[] selectionArgs = {billId};
        String[] columns = {"*"};
        Cursor cursor = db.query(false,CodeTable.TABLE,columns,selection,selectionArgs,null,null,null,null);

        if(cursor.getCount()>0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Code code = new Code();
                code.setCode(cursor.getString(cursor.getColumnIndex(CodeTable.Code)));
                code.setProductId(cursor.getString(cursor.getColumnIndex(CodeTable.GoodId)));
                code.setBatchId(cursor.getString(cursor.getColumnIndex(CodeTable.BatchId)));
                code.setWholesaleId(cursor.getString(cursor.getColumnIndex(CodeTable.WholesaleId)));
                code.setDealerId(cursor.getString(cursor.getColumnIndex(CodeTable.DealerId)));
                code.setShopId(cursor.getString(cursor.getColumnIndex(CodeTable.ShopId)));
                code.setCodeStyle(cursor.getInt(cursor.getColumnIndex(CodeTable.CodeStyle)));
                code.setBillId(cursor.getString(cursor.getColumnIndex(CodeTable.BillId)));
                code.setUpType(cursor.getInt(cursor.getColumnIndex(CodeTable.UpType)));
                code.setAdminId(cursor.getString(cursor.getColumnIndex(CodeTable.AdminId)));
                code.setProductId(cursor.getString(cursor.getColumnIndex(CodeTable.GoodId)));
                code.setUpDateString(cursor.getString(cursor.getColumnIndex(CodeTable.UpDateString)));
                code.setUpDateInt(cursor.getInt(cursor.getColumnIndex(CodeTable.UpDateInt)));
                code.setDelete(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(CodeTable.IsDelete))));
                codes.add(code);
            }
        }
        cursor.close();

        return codes;
    }

    /**
     *
     * @param code 条码
     * @param codeStyle 入出退
     * @return
     */
    public boolean haveCode(String code , int codeStyle){
        String selection = CodeTable.Code +" = ? and "+CodeTable.CodeStyle +" = ? ";
        String[] selectionArgs = {code,String.valueOf(codeStyle)};
        String[] columns = {"count(*)"};

        Cursor cursor = db.query(false,CodeTable.TABLE,columns,selection,selectionArgs,null,null,null,null);
        if(cursor.getCount()>0){
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                int count = cursor.getInt(cursor.getColumnIndex("count(*)"));

                if(count > 0){
                    return true;
                }
            }
        }
        cursor.close();

        return false;

    }
}
