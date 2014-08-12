package com.zendaimoney.android.athena.im.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zendaimoney.android.athena.data.ZendaiManager;

public class DataBaseHelper1 extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "user.db";
	private static final String USER_TABLE = "user_table";
	private static final String USER_ID = "_id";
	private static final String USER_NAME = "user_name";
	private static final String USER_PASSWORD = "user_pass";
	private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS"
			+ USER_TABLE
			+ "("
			+ USER_ID
			+ "INTEGER,"
			+ USER_NAME
			+ "TEXT,"
			+ USER_PASSWORD + "TEXT" + ")";

	public DataBaseHelper1(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_USER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	private SQLiteDatabase database = null;

	public SQLiteDatabase getdbase() {
		if (database == null)
			database = getWritableDatabase();
		return database;
	}

	public void addUser(ZendaiManager manager) {
		ContentValues values = new ContentValues();
		values.put(USER_ID, manager.getUserid());
		values.put(USER_NAME, manager.getUserName());
		values.put(USER_PASSWORD, manager.getUserPass());
		getdbase().insert(USER_TABLE, null, values);
	}

	public ZendaiManager getManager(String username) {
		String managercountQuery = "SELECT *FROM" + USER_TABLE + "WHERE "
				+ USER_NAME + "==" + username;
		Cursor rawQuery = getdbase().rawQuery(managercountQuery, null);
		if (rawQuery != null && rawQuery.getCount() > 0) {
			rawQuery.moveToFirst();
			return new ZendaiManager(rawQuery.getInt(rawQuery
					.getColumnIndex(USER_ID)), rawQuery.getString(rawQuery
					.getColumnIndex(USER_NAME)), rawQuery.getString(rawQuery
					.getColumnIndex(USER_PASSWORD)), null);
		}
		return null;
	}
}
