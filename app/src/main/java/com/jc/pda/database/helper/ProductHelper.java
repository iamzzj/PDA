package com.jc.pda.database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jc.pda.database.date.PdaDataBase;
import com.jc.pda.database.table.GoodTable;
import com.jc.pda.entity.Product;
import com.jc.pda.entity.UpDate;
import com.jc.pda.entitynet.ProductNet;
import com.jc.pda.utils.TimeUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2017/12/22.
 */

public class ProductHelper {
    private SQLiteDatabase db;

    private ProductHelper(Context context){
        db = PdaDataBase.getSingleton(context).getWritableDatabase();
    }

    private volatile static ProductHelper singleton;
    public static ProductHelper getSingleton(Context context) {
        if (singleton == null) {
            synchronized (ProductHelper.class) {
                if (singleton == null) {
                    singleton = new ProductHelper(context);
                }
            }
        }
        return singleton;
    }

    public void insertProducts(List<ProductNet> products){
        if (products!=null&&products.size()>0) {
            ContentValues values = new ContentValues();
            for (ProductNet product : products) {
                UpDate upDate = new UpDate();
                values.put(GoodTable.GoodId, product.getCID());
                values.put(GoodTable.GoodNo, product.getCGuestCode());
                values.put(GoodTable.GoodName, product.getCGuestName());
                values.put(GoodTable.IsDelete, String.valueOf(false));
                values.put(GoodTable.UpDateInt, upDate.getTime());
                values.put(GoodTable.UpDateString, upDate.getUpDate());
                db.insert(GoodTable.TABLE, null, values);

                values.clear();
            }
        }
    }

    public void deleteAll(){
        db.delete(GoodTable.TABLE, null, null);
    }

    public List<Product> getProducts(){
        List<Product> products = new ArrayList<>();

        String[] columns = {GoodTable.GoodNo,GoodTable.GoodName};
        Cursor cursor = db.query(false,GoodTable.TABLE,columns,null,null,null,null,null,null);

        if(cursor.getCount() > 0){
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                Product product = new Product();
                product.setProductNo(cursor.getString(cursor.getColumnIndex(GoodTable.GoodNo)));
                product.setProductName(cursor.getString(cursor.getColumnIndex(GoodTable.GoodName)));
                products.add(product);
            }
        }
        cursor.close();

        return products;
    }

    public int getCount(){
        String[] columns = {"count(*)"};
        Cursor cursor = db.query(false,GoodTable.TABLE,columns,null,null,null,null,null,null);

        int count = 0;

        if(cursor.getCount()>0){
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                count = cursor.getInt(cursor.getColumnIndex("count(*)"));
            }
        }
        cursor.close();

        return count;
    }

    public String getProductNameById(String productId){
        String productName = "未找到产品名字，资料变动。产品ID:"+productId;
        if(!TextUtils.isEmpty(productId)) {
            String selection = GoodTable.GoodNo + " = ?";
            String[] selectionArgs = {productId};
            String[] columns = {GoodTable.GoodName};
            Cursor cursor = db.query(true, GoodTable.TABLE, columns, selection, selectionArgs, null, null, null, null);

            if (cursor.getCount() > 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    productName = cursor.getString(cursor.getColumnIndex(GoodTable.GoodName));
                }
            }
            cursor.close();
        }

        return productName;
    }
}
