package com.jc.pda.database.date;

import android.Manifest;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import com.jc.pda.database.context.DatabasePathContext;
import com.jc.pda.database.table.BatchTable;
import com.jc.pda.database.table.BillTable;
import com.jc.pda.database.table.CodeTable;
import com.jc.pda.database.table.DealerTable;
import com.jc.pda.database.table.GoodTable;
import com.jc.pda.utils.Global;
import com.jc.pda.utils.Path;
import com.jc.pda.utils.PermissionUtils;
import com.orhanobut.logger.Logger;

import es.dmoral.toasty.Toasty;

public class PdaDataBase extends SQLiteOpenHelper{
	private static final String dbName = Global.NAME+".db";
	private static final int version = 1;

	private volatile static PdaDataBase singleton;

	public static PdaDataBase getSingleton(Context context) {
		if (singleton == null) {
			synchronized (PdaDataBase.class) {
				if (singleton == null) {
					DatabasePathContext dc = new DatabasePathContext(context, Path.getDataBasePath(context));
					singleton = new PdaDataBase(dc);
				}
			}
		}
		return singleton;
	}

	private PdaDataBase(Context context, String name, CursorFactory factory,
                       int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	private PdaDataBase(Context context, String name, CursorFactory factory,
                       int version) {
		super(context, name, factory, version);
	}
	
	private PdaDataBase(Context context){
		super(context, dbName, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String dealer = "create table "+ DealerTable.TABLE+" ( "
						+ DealerTable._id +" integer primary key autoincrement not null,"
						+ DealerTable.DealerId + " varchar(100) ,"
						+ DealerTable.DealerNo + " varchar(100) ,"
						+ DealerTable.DealerName + " varchar(100) ,"
						+ DealerTable.IsDelete + " bloe ,"
						+ DealerTable.UpDateString + " varcahr(100) ,"
						+ DealerTable.UpDateInt + " integer ,"
						+ DealerTable.NoteA + " varcahr(100) ,"
						+ DealerTable.NoteB + " varcahr(100) ,"
						+ DealerTable.NoteC + " varcahr(100) )";

		String batch = "create table "+ BatchTable.TABLE+" ( "
				+ BatchTable._id +" integer primary key autoincrement not null,"
				+ BatchTable.BatchId + " varchar(100) ,"
				+ BatchTable.BatchNo + " varchar(100) ,"
				+ BatchTable.BatchName + " varchar(100) ,"
				+ BatchTable.ProductId + " varchar(100) ,"
				+ BatchTable.IsDelete + " bloe ,"
				+ BatchTable.UpDateString + " varcahr(100) ,"
				+ BatchTable.UpDateInt + " integer ,"
				+ BatchTable.NoteA + " varcahr(100) ,"
				+ BatchTable.NoteB + " varcahr(100) ,"
				+ BatchTable.NoteC + " varcahr(100) )";

		String good = "create table "+ GoodTable.TABLE+" ( "
				+ GoodTable._id +" integer primary key autoincrement not null,"
				+ GoodTable.GoodId + " varchar(100) ,"
				+ GoodTable.GoodNo + " varchar(100) ,"
				+ GoodTable.GoodName + " varchar(100) ,"
				+ GoodTable.IsDelete + " bloe ,"
				+ GoodTable.UpDateString + " varcahr(100) ,"
				+ GoodTable.UpDateInt + " integer ,"
				+ GoodTable.NoteA + " varcahr(100) ,"
				+ GoodTable.NoteB + " varcahr(100) ,"
				+ GoodTable.NoteC + " varcahr(100) )";

		String code = "create table "+ CodeTable.TABLE+" ( "
				+ CodeTable._id +" integer primary key autoincrement not null,"
				+ CodeTable.Code + " varchar(100) ,"
				+ CodeTable.GoodId + " varchar(100) ,"
				+ CodeTable.BatchId + " varchar(100) ,"
				+ CodeTable.WholesaleId + " varchar(100) ,"
				+ CodeTable.DealerId + " varchar(100) ,"
				+ CodeTable.ShopId + " varchar(100) ,"
				+ CodeTable.CodeStyle + " integer ,"
				+ CodeTable.BillId + " varchar(100) ,"
				+ CodeTable.UpType + " integer ,"
				+ CodeTable.IsDelete + " bloe ,"
				+ CodeTable.AdminId + " varcahr(100) ,"
				+ CodeTable.UpDateString + " varcahr(100) ,"
				+ CodeTable.UpDateInt + " integer ,"
				+ CodeTable.NoteA + " varcahr(100) ,"
				+ CodeTable.NoteB + " varcahr(100) ,"
				+ CodeTable.NoteC + " varcahr(100) )";

		String bill = "create table "+ BillTable.TABLE+" ( "
				+ BillTable._id +" integer primary key autoincrement not null,"
				+ BillTable.BillId + " varchar(100) ,"
				+ BillTable.GoodId + " varchar(100) ,"
				+ BillTable.BatchId + " varchar(100) ,"
				+ BillTable.WholesaleId + " varchar(100) ,"
				+ BillTable.DealerId + " varchar(100) ,"
				+ BillTable.ShopId + " varchar(100) ,"
				+ BillTable.BillStyle + " integer ,"
				+ BillTable.UpType + " integer ,"
				+ BillTable.OldBillNo + " varchar(100) ,"
				+ BillTable.isUp + " bloe ,"
				+ BillTable.IsDelete + " bloe ,"
				+ BillTable.AdminId + " varcahr(100) ,"
				+ BillTable.UpDateString + " varcahr(100) ,"
				+ BillTable.UpDateInt + " integer ,"
				+ BillTable.NoteA + " varcahr(100) ,"
				+ BillTable.NoteB + " varcahr(100) ,"
				+ BillTable.NoteC + " varcahr(100) )";

		db.execSQL(dealer);
		db.execSQL(batch);
		db.execSQL(good);
		db.execSQL(code);
		db.execSQL(bill);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	
}
