package com.example.witquiz.databasemanager;

import java.util.ArrayList;
import java.util.List;

import com.example.witquiz.entities.Answer;
import com.example.witquiz.entities.Category;
import com.example.witquiz.entities.Question;

import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseManager {
	
	public static Category[] getAllCategories(boolean onlyWithQuestions){
		Cursor cursor = null;
		List<Category> categories;
		
		String mainQuery = "SELECT * FROM Categories ";
		
		if(onlyWithQuestions)
			mainQuery += " WHERE (SELECT COUNT(*) FROM Questions WHERE CategoryId = Categories.Id) > 0";
		
		try{
			cursor = DatabaseHelper.getDatabaseInstance().rawQuery(mainQuery, null);
			
			if(cursor.getCount()==0)
				return new Category[0];
			
			categories = new ArrayList<Category>(cursor.getCount());
			
			while(cursor.moveToNext()){
				
				categories.add(new Category(CursorHelper.getInt(cursor, "Id"), 
											CursorHelper.getString(cursor, "Name")));
			}
			
		}finally{
			if(cursor!= null)
				cursor.close();
		}
		
		return categories.toArray(new Category[categories.size()]);
	}
	
	public static Question[] getGameQuestionsForCategory(int categoryId){
		
		List<Question> questions = new ArrayList<Question>();
		Cursor cursor = null;
		
		try{
			cursor = DatabaseHelper.getDatabaseInstance().rawQuery(
					"SELECT q.Id, q.Question, q.AnswerId FROM Questions q WHERE q.CategoryId = ? ORDER BY RANDOM()"
						, new String[]{ String.valueOf(categoryId) } );
			
			if(cursor.getCount()==0)
				return questions.toArray(new Question[0]);
			
			while(cursor.moveToNext()){
				
				Question question = new Question(CursorHelper.getInt(cursor, "Id"), 
						categoryId, 
						CursorHelper.getString(cursor, "Question"), 
						null, 
						CursorHelper.getInt(cursor, "AnswerId"));		
					
				question.setAnswers(getAnswersForQuestion(question.getId(), true));
			
				questions.add(question);
			}
			
		} finally{
			
			if(cursor!= null)
				cursor.close();
		}
		
		return questions.toArray(new Question[questions.size()]);
	}
	
	public static Answer[] getAnswersForQuestion(int questionId, boolean randomOrder){
		Cursor answerCursor = null;
		Answer[] answers = new Answer[4];
		
		try{
			String query = "SELECT a.Id, a.Answer FROM Answers a WHERE a.QuestionId = ? ";
			
			if(randomOrder)
				query += "ORDER BY RANDOM()";
			
			answerCursor = DatabaseHelper.getDatabaseInstance().rawQuery(
								query, new String[]{ String.valueOf(questionId) } );

			
			int i = 0;
			
			while(answerCursor.moveToNext()){
				answers[i] = new Answer(CursorHelper.getInt(answerCursor, "Id"), 
						questionId, 
						CursorHelper.getString(answerCursor, "Answer"));
				
				++i;
			}
		} finally{
			
			if(answerCursor!= null)
				answerCursor.close();
		}
		
			return answers;
	}
	
	public static Cursor getQuestionsForCategory(int categoryId){
		return  DatabaseHelper.getDatabaseInstance()
				.rawQuery("SELECT q.Id AS _id, q.Question, (SELECT group_concat(Answer, ', ') FROM Answers WHERE QuestionId = q.Id ) AS Answers" +
						" FROM Questions q" +
						" WHERE q.CategoryId = ?",
							new String[]{ String.valueOf(categoryId) } );
	}
	
	public static Question getQuestionById(int questionId){
		Question question = new Question();
		Cursor cursor = null;
		
		try{
			cursor = DatabaseHelper.getDatabaseInstance().rawQuery("SELECT CategoryId, Question, AnswerId FROM Questions WHERE Id = ?", 
						new String[]{ String.valueOf(questionId) } );
			
			if(cursor.moveToNext()){
				question.setId(questionId);
				question.setQuestion(CursorHelper.getString(cursor, "Question"));
				question.setCategoryId(CursorHelper.getInt(cursor, "CategoryId"));
				question.setAnswerId(CursorHelper.getInt(cursor, "AnswerId"));
				
				question.setAnswers(getAnswersForQuestion(question.getId(), false));
			}
			
		} finally{
			if(cursor != null)
				cursor.close();
		}
		
		return question;
	}
	
	public static Category createNewCategory(String categoryName){
		Category category = new Category(0, categoryName);
		
		ContentValues values = new ContentValues();
		
		values.put("Name", categoryName);
		
		category.setId((int)DatabaseHelper.getDatabaseInstance().insert("Categories", null, values));
		
		return category;
	}
	
	public static void insertQuestion(Question question){
		
		ContentValues values = new ContentValues();
		
		values.put("CategoryId", question.getCategoryId());
		values.put("Question", question.getQuestion());
		values.put("AnswerId", question.getAnswerId());
		
		DatabaseHelper.getDatabaseInstance().beginTransaction();
		try{
			
			question.setId((int) DatabaseHelper.getDatabaseInstance().insert("Questions", null, values));
			
			Answer[] answers = question.getAnswers();
			
			for(int i=0; i<4 ;++i){
				ContentValues answerValues = new ContentValues();
				
				answerValues.put("QuestionId", question.getId());
				answerValues.put("Answer", answers[i].getAnswer());
				
				int answerId = (int) DatabaseHelper.getDatabaseInstance().insert("Answers", null, answerValues);
				
				if(question.getAnswerId() == i){
					ContentValues correctAnswerValues = new ContentValues();
					correctAnswerValues.put("AnswerId", answerId);
					
					DatabaseHelper.getDatabaseInstance().update("Questions", correctAnswerValues, "Id = ?", 
							new String[] { String.valueOf(question.getId()) } );
				}
			}
			
			DatabaseHelper.getDatabaseInstance().setTransactionSuccessful();
			
		}
		finally{
			DatabaseHelper.getDatabaseInstance().endTransaction();
		}
		
	}

	public static void updateQuestion(Question question){
		
		ContentValues values = new ContentValues();
		
		values.put("CategoryId", question.getCategoryId());
		values.put("Question", question.getQuestion());
		values.put("AnswerId", question.getAnswerId());
		
		DatabaseHelper.getDatabaseInstance().beginTransaction();
		
		try{
			
			DatabaseHelper.getDatabaseInstance().update("Questions", values, "Id = ?", 
					new String[] { String.valueOf(question.getId()) } );
			
			Answer[] answers = question.getAnswers();
			
			for(int i=0; i<4 ;++i){
				ContentValues answerValues = new ContentValues();
				
				answerValues.put("QuestionId", answers[i].getQuestionId());
				answerValues.put("Answer", answers[i].getAnswer());
				
				DatabaseHelper.getDatabaseInstance().update("Answers", answerValues, "Id = ?", 
						new String[] { String.valueOf(answers[i].getId()) } );
			}
			
			DatabaseHelper.getDatabaseInstance().setTransactionSuccessful();
			
		}
		finally{
			DatabaseHelper.getDatabaseInstance().endTransaction();
		}		
		
	}
	
	public static void deleteQuestion(Question question){
		DatabaseHelper.getDatabaseInstance().beginTransaction();
		
		try{

			DatabaseHelper.getDatabaseInstance().delete("Answers", "QuestionId = ?", 
						new String[] {String.valueOf(question.getId())});

			
			DatabaseHelper.getDatabaseInstance().delete("Questions", "Id = ?", 
					new String[] {String.valueOf(question.getId())});
			
			
			DatabaseHelper.getDatabaseInstance().setTransactionSuccessful();
		}
		finally{
			DatabaseHelper.getDatabaseInstance().endTransaction();
		}

	}
	
	public static void saveHighScore(String userName, int highScore){
		ContentValues values = new ContentValues();
		
		values.put("UserName", userName);
		values.put("HighScore", highScore);
			
		Cursor cursor = null;
		
		try{
			cursor = DatabaseHelper.getDatabaseInstance().rawQuery("SELECT HighScore FROM HighScores WHERE UserName = ?", 
				new String[]{userName} );
			
			if(cursor.moveToNext()){
				
				if(highScore > CursorHelper.getInt(cursor, "HighScore"))
					DatabaseHelper.getDatabaseInstance().update("HighScores", values, "UserName = ?", new String[]{userName});
			
			}
			else{
				DatabaseHelper.getDatabaseInstance().insert("HighScores", null, values);
			}
		
		} finally{
			if(cursor != null)
				cursor.close();
		}
	}
	
	public static Cursor getHighScores(){
		return  DatabaseHelper.getDatabaseInstance()
				.rawQuery("SELECT rowid AS _id, scores.UserName, scores.HighScore " +
						"FROM HighScores scores ORDER BY scores.HighScore DESC ",
							null);
	}
}
