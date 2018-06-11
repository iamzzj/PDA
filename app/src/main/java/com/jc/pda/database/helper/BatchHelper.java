package com.jc.pda.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jc.pda.database.date.PdaDataBase;
import com.jc.pda.database.table.BatchTable;
import com.jc.pda.database.table.DealerTable;
import com.jc.pda.entity.Batch;
import com.jc.pda.entity.UpDate;
import com.jc.pda.entitynet.BatchNet;
import com.jc.pda.entitynet.ProductNet;
import com.jc.pda.utils.TimeUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2017/12/26.
 */

public class BatchHelper {
    private SQLiteDatabase db;

    private BatchHelper(Context context){
        db = PdaDataBase.getSingleton(context).getWritableDatabase();;
    }

    private volatile static BatchHelper singleton;

    public static BatchHelper getSingleton(Context context) {
        if (singleton == null) {
            synchronized (BatchHelper.class) {
                if (singleton == null) {
                    singleton = new BatchHelper(context);
                }
            }
        }
        return singleton;
    }

    public void insertBatchs(List<BatchNet> batchNets, ProductNet productNet){
        if(batchNets!=null&&batchNets.size()>0){
            ContentValues values = new ContentValues();
            for(BatchNet batchNet : batchNets){
                UpDate upDate = new UpDate();
                values.put(BatchTable.BatchId,batchNet.getCID());
                values.put(BatchTable.BatchNo,batchNet.getCGuestCode());
                values.put(BatchTable.BatchName,batchNet.getCGuestName());
                values.put(BatchTable.ProductId,productNet.getCGuestCode());
                values.put(BatchTable.IsDelete,String.valueOf(false));
                values.put(BatchTable.UpDateInt,upDate.getTime());
                values.put(BatchTable.UpDateString, upDate.getUpDate());
                db.insert(BatchTable.TABLE,null,values);
                values.clear();
            }
        }
    }

    public void deleteAll(){
        db.delete(BatchTable.TABLE,null,null);
    }

    public List<Batch> getBatchs(String productId){
        List<Batch> batches = new ArrayList<>();
        String selection = BatchTable.ProductId +" = ?";
        String[] selectionArgs = {productId};
        String[] columns = {BatchTable.BatchNo,BatchTable.BatchName};
        Cursor cursor = db.query(false,BatchTable.TABLE,columns,selection,selectionArgs,null,null,null,null);

        if(cursor.getCount()>0){
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                Batch batch = new Batch();
                batch.setBatchNo(cursor.getString(cursor.getColumnIndex(BatchTable.BatchName)));
                batch.setBatchName(cursor.getString(cursor.getColumnIndex(BatchTable.BatchName)));
                batches.add(batch);
            }
        }
        cursor.close();

        return batches;
    }

    public int getCount(){
        String[] columns = {"count(*)"};
        Cursor cursor = db.query(false,BatchTable.TABLE,columns,null,null,null,null,null,null);

        int count = 0;

        if(cursor.getCount()>0){
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                count = cursor.getInt(cursor.getColumnIndex("count(*)"));
            }
        }
        cursor.close();

        return count;
    }
}
