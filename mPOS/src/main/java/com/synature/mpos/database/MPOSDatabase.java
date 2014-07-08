package com.synature.mpos.database;

import java.util.UUID;

import com.synature.mpos.database.table.BankTable;
import com.synature.mpos.database.table.BaseColumn;
import com.synature.mpos.database.table.ComputerTable;
import com.synature.mpos.database.table.CreditCardTable;
import com.synature.mpos.database.table.GlobalPropertyTable;
import com.synature.mpos.database.table.HeaderFooterReceiptTable;
import com.synature.mpos.database.table.LanguageTable;
import com.synature.mpos.database.table.MenuCommentGroupTable;
import com.synature.mpos.database.table.MenuCommentTable;
import com.synature.mpos.database.table.MenuFixCommentTable;
import com.synature.mpos.database.table.OrderCommentTable;
import com.synature.mpos.database.table.OrderDetailTable;
import com.synature.mpos.database.table.OrderSetTable;
import com.synature.mpos.database.table.OrderTransactionTable;
import com.synature.mpos.database.table.PayTypeTable;
import com.synature.mpos.database.table.PaymentButtonTable;
import com.synature.mpos.database.table.PaymentDetailTable;
import com.synature.mpos.database.table.PrintReceiptLogTable;
import com.synature.mpos.database.table.ProductComponentGroupTable;
import com.synature.mpos.database.table.ProductComponentTable;
import com.synature.mpos.database.table.ProductDeptTable;
import com.synature.mpos.database.table.ProductGroupTable;
import com.synature.mpos.database.table.ProductTable;
import com.synature.mpos.database.table.SessionDetailTable;
import com.synature.mpos.database.table.SessionTable;
import com.synature.mpos.database.table.ShopTable;
import com.synature.mpos.database.table.StaffPermissionTable;
import com.synature.mpos.database.table.StaffTable;
import com.synature.mpos.database.table.SyncMasterLogTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author j1tth4
 * 
 */
public class MPOSDatabase extends BaseColumn{
	
	public static final int NOT_SEND = 0;
	public static final int ALREADY_SEND = 1;
	
	private MPOSOpenHelper mHelper;
	
	public MPOSDatabase(Context context){
		mHelper = MPOSOpenHelper.getInstance(context); 
	}
	
	public String getUUID(){
		return UUID.randomUUID().toString();
	}
	
	public SQLiteDatabase getWritableDatabase(){
		return mHelper.getWritableDatabase();
	}
	
	public SQLiteDatabase getReadableDatabase(){
		return mHelper.getReadableDatabase();
	}
	
	public static class MPOSOpenHelper extends SQLiteOpenHelper {
		
		private static final String DB_NAME = "mpos.db";
		private static final int DB_VERSION = 7;

		private static MPOSOpenHelper sHelper;

		/**
		 * @param context
		 * @return SQLiteOpenHelper instance This singleton pattern for only get
		 *         one SQLiteOpenHelper instance for thread save
		 */
		public static synchronized MPOSOpenHelper getInstance(Context context) {
			if (sHelper == null) {
				sHelper = new MPOSOpenHelper(context.getApplicationContext());
			}
			return sHelper;
		}

		private MPOSOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			BankTable.onCreate(db);
			ComputerTable.onCreate(db);
			CreditCardTable.onCreate(db);
			GlobalPropertyTable.onCreate(db);
			LanguageTable.onCreate(db);
			HeaderFooterReceiptTable.onCreate(db);
			MenuCommentTable.onCreate(db);
			MenuCommentGroupTable.onCreate(db);
			MenuFixCommentTable.onCreate(db);
			OrderDetailTable.onCreate(db);
			OrderTransactionTable.onCreate(db);
			OrderSetTable.onCreate(db);
			OrderCommentTable.onCreate(db);
			PrintReceiptLogTable.onCreate(db);
			PaymentDetailTable.onCreate(db);
			PaymentButtonTable.onCreate(db);
			PayTypeTable.onCreate(db);
			ProductDeptTable.onCreate(db);
			ProductGroupTable.onCreate(db);
			ProductComponentGroupTable.onCreate(db);
			ProductComponentTable.onCreate(db);
			ProductTable.onCreate(db);
			SessionTable.onCreate(db);
			SessionDetailTable.onCreate(db);
			ShopTable.onCreate(db);
			StaffPermissionTable.onCreate(db);
			StaffTable.onCreate(db);
			SyncMasterLogTable.onCreate(db);
		}

		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion,
				int newVersion) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			BankTable.onUpgrade(db, oldVersion, newVersion);
			ComputerTable.onUpgrade(db, oldVersion, newVersion);
			CreditCardTable.onUpgrade(db, oldVersion, newVersion);
			GlobalPropertyTable.onUpgrade(db, oldVersion, newVersion);
			LanguageTable.onUpgrade(db, oldVersion, newVersion);
			HeaderFooterReceiptTable.onUpgrade(db, oldVersion, newVersion);
			MenuCommentTable.onUpgrade(db, oldVersion, newVersion);
			MenuCommentGroupTable.onUpgrade(db, oldVersion, newVersion);
			MenuFixCommentTable.onUpgrade(db, oldVersion, newVersion);
			PrintReceiptLogTable.onUpgrade(db, oldVersion, newVersion);
			PaymentDetailTable.onUpgrade(db, oldVersion, newVersion);
			PaymentButtonTable.onUpgrade(db, oldVersion, newVersion);
			PayTypeTable.onUpgrade(db, oldVersion, newVersion);
			ProductDeptTable.onUpgrade(db, oldVersion, newVersion);
			ProductGroupTable.onUpgrade(db, oldVersion, newVersion);
			ProductComponentGroupTable.onUpgrade(db, oldVersion, newVersion);
			ProductComponentTable.onUpgrade(db, oldVersion, newVersion);
			ProductTable.onUpgrade(db, oldVersion, newVersion);
			ShopTable.onUpgrade(db, oldVersion, newVersion);
			StaffPermissionTable.onUpgrade(db, oldVersion, newVersion);
			StaffTable.onUpgrade(db, oldVersion, newVersion);
			SyncMasterLogTable.onUpgrade(db, oldVersion, newVersion);
		}
	}
}
