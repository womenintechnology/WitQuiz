package com.example.witquiz.databasemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper {
	
	private static SQLiteDatabase database;
	private static DatabaseOpenHelper dbOpenHelper;
	private static Context context;
	
	private DatabaseHelper(){
		
	}
	
	public static SQLiteDatabase getDatabaseInstance(){
		
		if (dbOpenHelper == null)
            dbOpenHelper = new DatabaseOpenHelper(context);

		if (database == null) 
	        database = dbOpenHelper.getWritableDatabase();
	    
	    return database;
	}
	
	public static void setDatabaseManagerContext(Context context){
		DatabaseHelper.context = context;
	}
	
	public static void close(){
		
		if(database != null && database.isOpen())
			database.close();
		
		dbOpenHelper = null;
		database = null;
	}

}
