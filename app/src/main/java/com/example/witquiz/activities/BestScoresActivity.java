package com.example.witquiz.activities;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.witquiz.R;
import com.example.witquiz.databasemanager.CursorHelper;
import com.example.witquiz.databasemanager.DatabaseManager;

public class BestScoresActivity extends AppCompatActivity {

    ListView scoresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_scores);

        loadControls();
    }

    private void loadControls(){

        scoresList = (ListView) findViewById(R.id.scores_listView);
        CursorAdapter adapter = new CursorAdapter(this, DatabaseManager.getHighScores()) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {

                return LayoutInflater.from(context).inflate(R.layout.best_score_row, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView numberTextView = (TextView) view.findViewById(R.id.position_textView);
                TextView userNameTextView = (TextView) view.findViewById(R.id.username_textView);
                TextView scoreTextView = (TextView) view.findViewById(R.id.score_textView);

                numberTextView.setText(CursorHelper.getString(cursor, "_id") + ".");
                userNameTextView.setText(CursorHelper.getString(cursor, "UserName"));
                scoreTextView.setText(String.valueOf(CursorHelper.getInt(cursor, "HighScore")));

            }

            @Override
            public boolean isEnabled(int position) {
                return false;
            }
        };

        scoresList.setAdapter(adapter);

        View header = LayoutInflater.from(this).inflate(R.layout.best_score_row, null, false);
        header.setBackgroundResource(R.drawable.question_button_selector);
        ((TextView) header.findViewById(R.id.position_textView)).setText(R.string.number);
        ((TextView) header.findViewById(R.id.username_textView)).setText(R.string.username_header);
        ((TextView) header.findViewById(R.id.score_textView)).setText(R.string.score);

        scoresList.addHeaderView(header, null, false);
    }
}
