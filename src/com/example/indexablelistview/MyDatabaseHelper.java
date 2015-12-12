package com.example.indexablelistview;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper
{
	private Context mContext;
	private static final String CREATE_BOOK = "create table Book("
			+ "id integer primary key autoincrement,"
			+ "author text,"
			+ "price real,"
			+ "pages integer,"
			+ "name text)";

	public MyDatabaseHelper(Context context, String name,
			CursorFactory factory, int version)
	{
		super(context, name, factory, version);
		mContext = context;
	}

	public MyDatabaseHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler)
	{
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_BOOK);
		Toast.makeText(mContext, "create succeeded", Toast.LENGTH_SHORT).show();
		Log.d("old", "create succeeded");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
	}

}
