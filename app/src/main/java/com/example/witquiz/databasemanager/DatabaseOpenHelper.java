package com.example.witquiz.databasemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DatabaseOpenHelper extends SQLiteOpenHelper{
	
	private static String DB_PATH = Environment.getExternalStorageDirectory().toString()+"/";
	private static String DB_NAME = "witData.db";
	public static final int DBVERSION = 1;

	public DatabaseOpenHelper(Context context) {
		
		super(context, DB_PATH + DB_NAME, null, DBVERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE Categories( " +
				"Id INTEGER NOT NULL, " +
				"Name NVARCHAR(100) NOT NULL, " +
				"PRIMARY KEY (Id)" +
				");");
		
		db.execSQL("CREATE TABLE Questions( " +
				"Id INTEGER NOT NULL, " +
				"CategoryId INT NOT NULL, " +
				"Question NVARCHAR(100) NOT NULL, " +
				"AnswerId INT NOT NULL, " +
				"PRIMARY KEY(Id) " +
				");");
		
		db.execSQL("CREATE TABLE Answers( " +
				"Id INTEGER NOT NULL, " +
				"QuestionId INT NOT NULL, " +
				"Answer NVARCHAR(100) NOT NULL, " +
				"PRIMARY KEY (Id) " +
				");");
		
		db.execSQL("CREATE TABLE Users( " +
				"Id INTEGER NOT NULL, " +
				"Name NVARCHAR(100) NOT NULL, " +
				"PRIMARY KEY (Id) " +
				");");
		
		db.execSQL("CREATE TABLE HighScores( " +
				"UserName NVARCHAR(100) NOT NULL, " +
				"HighScore INT NOT NULL, " +
				"PRIMARY KEY (UserName) " +
				");");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

}
