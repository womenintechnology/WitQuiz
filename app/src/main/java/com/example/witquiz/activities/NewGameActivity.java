package com.example.witquiz.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.witquiz.R;
import com.example.witquiz.databasemanager.DatabaseManager;
import com.example.witquiz.entities.Category;
import com.example.witquiz.entities.Question;

public class NewGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        loadControls();
    }

    private void loadControls(){

        final Spinner categorySpinner = (Spinner) findViewById(R.id.choose_category_spinner);

        Category[] categories = DatabaseManager.getAllCategories(true);

        categorySpinner.setAdapter(new ArrayAdapter<Category>(this, R.layout.simple_row, categories){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if(convertView == null){
                    convertView = NewGameActivity.this.getLayoutInflater()
                            .inflate(R.layout.simple_row, null);
                }

                TextView categoryTextView = (TextView) convertView.findViewById(R.id.simple_row_textView);

                categoryTextView.setText(this.getItem(position).getName());

                return convertView;
            }

            @Override
            public View getDropDownView (int position, View convertView, ViewGroup parent){

                if(convertView == null){
                    convertView = NewGameActivity.this.getLayoutInflater()
                            .inflate(R.layout.simple_row, null);
                }

                TextView categoryTextView = (TextView) convertView.findViewById(R.id.simple_row_textView);

                categoryTextView.setText(this.getItem(position).getName());

                return convertView;

            }

        });

        Button startGameButton = (Button) findViewById(R.id.start_game_button);

        startGameButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(categorySpinner.getCount() == 0){
                    Toast.makeText(NewGameActivity.this, R.string.no_categories_to_choose, Toast.LENGTH_LONG).show();
                    return;
                }

                Category selectedCategory = (Category) categorySpinner.getSelectedItem();

                Question[] questions = DatabaseManager.getGameQuestionsForCategory(selectedCategory.getId());

                Intent intent = new Intent(NewGameActivity.this, GameActivity.class);
                intent.putExtra("categoryName", selectedCategory.getName());
                intent.putExtra("questions", questions);
                startActivity(intent);

            }
        });
    }
}
