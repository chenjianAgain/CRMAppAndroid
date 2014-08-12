package com.zendaimoney.android.athena.im.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.zendaimoney.android.athena.AppLog;

/**
 * SQLite数据库的帮助类
 * 
 * 该类属于扩展类,主要承担数据库初始化和版本升级使用,其他核心全由核心父类完成
 * 
 * @author shimiso
 * 
 */
public class DataBaseHelper extends SDCardSQLiteOpenHelper {

	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		
		super(context, name, factory, version);
		AppLog.i("#########", "========DataBaseHelper=======name="+name+"-----version="+version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL("CREATE TABLE [im_msg_his] ([_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, [content] NVARCHAR, [msg_from] NVARCHAR, [msg_to] NVARCHAR, [msg_time] TEXT, [msg_type] INTEGER);");

		db.execSQL("CREATE TABLE [im_msg_his] ([_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, [content] NVARCHAR, [msg_from] NVARCHAR, [msg_to] NVARCHAR, [msg_time] TEXT, [types] INTEGER, [msg_type] INTEGER, [msg_status] INTEGER);");
		db.execSQL("CREATE TABLE [im_notice]  ([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [type] INTEGER, [title] NVARCHAR, [content] NVARCHAR, [notice_from] NVARCHAR, [notice_to] NVARCHAR, [notice_time] TEXT, [status] INTEGER);");
	
		AppLog.i("#########", "========DataBaseHelper=======onCreate=");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		AppLog.i("#########", "========DataBaseHelper=======onUpgrade=1");
		db.execSQL("DROP TABLE [im_msg_his] ;");
		db.execSQL("DROP TABLE [im_notice] ;");
		db.execSQL("CREATE TABLE [im_msg_his] ([_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, [content] NVARCHAR, [msg_from] NVARCHAR, [msg_to] NVARCHAR, [msg_time] TEXT, [types] INTEGER, [msg_type] INTEGER, [msg_status] INTEGER);");	
		db.execSQL("CREATE TABLE [im_notice]  ([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [type] INTEGER, [title] NVARCHAR, [content] NVARCHAR, [notice_from] NVARCHAR, [notice_to] NVARCHAR, [notice_time] TEXT, [status] INTEGER);");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}
