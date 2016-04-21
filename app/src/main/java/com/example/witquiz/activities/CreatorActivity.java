package com.example.witquiz.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.witquiz.R;
import com.example.witquiz.databasemanager.DatabaseManager;
import com.example.witquiz.entities.Category;

public class CreatorActivity extends AppCompatActivity {

    private RadioButton editCategoryRadio;
    private RadioButton newCategoryRadio;

    private Spinner categorySpinner;
    private EditText newCategoryEditText;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);

        loadControls();
    }

    @Override
    protected void onResume() {

        super.onResume();

        Category[] categories = DatabaseManager.getAllCategories(false);

        categorySpinner.setAdapter(new ArrayAdapter<Category>(this, R.layout.simple_row, categories){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if(convertView == null){
                    convertView = CreatorActivity.this.getLayoutInflater()
                            .inflate(R.layout.simple_row, null);
                }

                TextView categoryTextView = (TextView) convertView.findViewById(R.id.simple_row_textView);

                categoryTextView.setText(this.getItem(position).getName());

                return convertView;
            }

            @Override
            public View getDropDownView (int position, View convertView, ViewGroup parent){

                if(convertView == null){
                    convertView = CreatorActivity.this.getLayoutInflater()
                            .inflate(R.layout.simple_row, null);
                }

                TextView categoryTextView = (TextView) convertView.findViewById(R.id.simple_row_textView);

                categoryTextView.setText(this.getItem(position).getName());

                return convertView;

            }

        });
    }

    private void loadControls() {
        editCategoryRadio = (RadioButton) findViewById(R.id.edit_category_radioButton);
        newCategoryRadio = (RadioButton) findViewById(R.id.new_category_radioButton);

        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        newCategoryEditText = (EditText) findViewById(R.id.new_category_editText);
        nextButton = (Button) findViewById(R.id.creator_next_button);

        categorySpinner.setEnabled(editCategoryRadio.isChecked());
        newCategoryEditText.setEnabled(newCategoryRadio.isChecked());


        editCategoryRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                categorySpinner.setEnabled(isChecked);
            }
        });

        newCategoryRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                newCategoryEditText.setEnabled(isChecked);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Category selectedCategory;

                if(categorySpinner.isEnabled()){

                    if(categorySpinner.getCount() == 0){
                        Toast.makeText(CreatorActivity.this, R.string.no_categories_to_edit, Toast.LENGTH_LONG).show();
                        return;
                    }

                    selectedCategory = (Category) categorySpinner.getSelectedItem();
                }
                else{

                    String newCategoryName = newCategoryEditText.getText().toString();

                    for(int i =0; i< categorySpinner.getCount(); ++i){

                        Category cat = (Category) categorySpinner.getItemAtPosition(i);

                        if(cat.getName().equals(newCategoryName)){
                            Toast.makeText(CreatorActivity.this, R.string.category_name_exists, Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    if(newCategoryName.isEmpty()){
                        Toast.makeText(CreatorActivity.this, R.string.category_name_cannot_be_empty, Toast.LENGTH_LONG).show();
                        return;
                    }

                    selectedCategory = DatabaseManager.createNewCategory(newCategoryName);
                }

                Intent intent = new Intent(CreatorActivity.this, CategoryActivity.class);
                intent.putExtra("category", selectedCategory);
                startActivity(intent);

            }
        });

    }
}
