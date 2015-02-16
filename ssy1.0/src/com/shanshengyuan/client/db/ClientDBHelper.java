package com.shanshengyuan.client.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shanshengyuan.client.data.BuyCar;
import com.shanshengyuan.client.data.SearchHistory;

/**
 * sqlite3 /data/data/com.zhubajie.client/databases/clientdatabase
 * 
 * @author Jelly
 */

public class ClientDBHelper extends SQLiteOpenHelper {

	private static ClientDBHelper instance;
	private static final String tag = ClientDBHelper.class.getSimpleName();
	public static final String DATABASE_NAME = "clientdatabase";
	public static final int DATABASE_VERSION = 3;

	public static final ClientDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new ClientDBHelper(context);
		}
		return instance;
	}

	public ClientDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public SQLiteDatabase getDb() {
		return this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		SearchHistory.table.dropTable(db);
		SearchHistory.table.createTable(db);
		BuyCar.table.dropTable(db);
		BuyCar.table.createTable(db);
		db.setLockingEnabled(false);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if (oldVersion < 3 && newVersion > oldVersion) {

			// SearchHistoryKey.table.dropTable(db);
			// SearchHistoryKey.table.createTable(db);

			}

	}
}
