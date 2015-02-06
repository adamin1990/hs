package com.iiijiaban.com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
	/**
	 * 数据库名称
	 */
  private static final String DB_NAME="hairstyle.db";
	/**
	 * 数据库版本
	 */
	private static final int VERSION = 1;
	private SQLiteDatabase database;
	//构造器
	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	public DbHelper(Context context){
		super(context, DB_NAME, null, VERSION);
	}
//必须重写的父类方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql="create table hairstyled(id integer primary key autoincrement,isrc varchar(255),msg varchar(255))";
		this.database = db;
		database.execSQL(sql);
	}
	//必须重写的父类方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists table hairstyled"); 
		onCreate(db);
	}
	public Cursor query() {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.query("hairstyled", null, null, null, null, null, null);
		return c;
	}
	public SQLiteDatabase getSQLiteDatabase() {
		database = getWritableDatabase();
		return database;
	}
/**
 * 插入表 
 * @param contentValues
 */
	public void insert(ContentValues contentValues) {
		SQLiteDatabase db = getWritableDatabase();
		db.insert("hairstyled", null, contentValues);
		db.close();
	}
	/**
	 * 删除特定字段
	 * @param path
	 */
	public void del(String path) {
		database = getWritableDatabase();
		database.delete("hairstyled", "isrc=?", new String[] { path });
	}
	/**
	 * 清除表
	 */
	public void delAll(){
		database = getWritableDatabase();
		database.delete("hairstyled", null, null);
	}
}
