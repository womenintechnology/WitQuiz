package com.example.witquiz.activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.witquiz.R;
import com.example.witquiz.databasemanager.CursorHelper;
import com.example.witquiz.databasemanager.DatabaseManager;
import com.example.witquiz.entities.Category;
import com.example.witquiz.entities.Question;

public class CategoryActivity extends AppCompatActivity {

    ListView questionsListView;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        category = this.getIntent().getExtras().getParcelable("category");

        loadControls();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_new_question) {

            // start new activity to add question
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((CursorAdapter)questionsListView.getAdapter())
                .changeCursor(DatabaseManager.getQuestionsForCategory(category.getId()));
    }

    private void loadControls() {

        TextView categoryTextView = (TextView) findViewById(R.id.category_name_textView);
        categoryTextView.setText(category.getName());

        questionsListView = (ListView) findViewById(R.id.questions_listView);

        CursorAdapter adapter = new CursorAdapter(this, DatabaseManager.getQuestionsForCategory(category.getId())) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {

                return LayoutInflater.from(context).inflate(R.layout.question_row, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView questionTextView = (TextView) view.findViewById(R.id.question_summary_textView);
                questionTextView.setText(CursorHelper.getString(cursor, "Question"));

                TextView answersTextView = (TextView) view.findViewById(R.id.answers_summary_textView);
                answersTextView.setText(CursorHelper.getString(cursor, "Answers"));

                view.setTag(CursorHelper.getInt(cursor, "_id"));
            }
        };

        questionsListView.setAdapter(adapter);

        questionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Question selectedQuestion = DatabaseManager.getQuestionById((int) id);

                // start new activity to edit question
            }

        });
    }

}
