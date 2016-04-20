package com.example.witquiz.databasemanager;

import android.database.Cursor;

public class CursorHelper {
	
	public static int getInt(Cursor cursor, String name){
		
		return cursor.getInt(cursor.getColumnIndex(name));
	}
	
	public static String getString(Cursor cursor, String name){
		
		return cursor.getString(cursor.getColumnIndex(name));
	}

}
