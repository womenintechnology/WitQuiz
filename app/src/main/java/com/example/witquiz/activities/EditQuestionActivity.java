package com.example.witquiz.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.witquiz.R;
import com.example.witquiz.databasemanager.DatabaseManager;
import com.example.witquiz.entities.Answer;
import com.example.witquiz.entities.Category;
import com.example.witquiz.entities.Question;

public class EditQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    Question question;
    private EditText questionTextView;
    private RadioButton[] answerButtons;
    private EditText[] answerEditTexts;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        if(savedInstanceState != null){
            question = savedInstanceState.getParcelable("question");
            category = savedInstanceState.getParcelable("category");
        }
        else{
            question = getIntent().getExtras().getParcelable("question");
            category = getIntent().getExtras().getParcelable("category");
        }

        loadControls();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_question_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.save_question) {
            boolean newQuestion = question == null;

            if(newQuestion){
                question =  new Question();

                for(int i = 0; i< 4; ++i){
                    Answer answer = new Answer(0, 0, answerEditTexts[i].getText().toString());

                    question.getAnswers()[i] = answer;

                    if(answerButtons[i].isChecked())
                        question.setAnswerId(i);
                }
            }
            else{

                for(int i = 0; i< 4; ++i){

                    question.getAnswers()[i].setAnswer(answerEditTexts[i].getText().toString());

                    if(answerButtons[i].isChecked())
                        question.setAnswerId(question.getAnswers()[i].getId());
                }
            }

            question.setCategoryId(category.getId());
            question.setQuestion(questionTextView.getText().toString());

            if(newQuestion)
                DatabaseManager.insertQuestion(question);
            else
                DatabaseManager.updateQuestion(question);

            this.finish();

            return true;
        }
        else if(id == R.id.delete_question){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage(R.string.do_you_want_to_delete_question)
                    .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if(question != null)
                                DatabaseManager.deleteQuestion(question);

                            EditQuestionActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("question", question);
        outState.putParcelable("category", category);

        super.onSaveInstanceState(outState);
    }

    private void loadControls(){

        ((TextView) findViewById(R.id.category_name_textView)).setText(category.getName()) ;

        questionTextView = (EditText) findViewById(R.id.edit_question_editText);
        answerButtons = new RadioButton[4];
        answerEditTexts = new EditText[4];

        answerButtons[0] = (RadioButton) findViewById(R.id.answerA_radioButton);
        answerButtons[1] = (RadioButton) findViewById(R.id.answerB_radioButton);
        answerButtons[2] = (RadioButton) findViewById(R.id.answerC_radioButton);
        answerButtons[3] = (RadioButton) findViewById(R.id.answerD_radioButton);

        for(int i =0; i< 4; ++i)
            answerButtons[i].setOnClickListener(this);

        answerEditTexts[0] = (EditText) findViewById(R.id.answerA_editText);
        answerEditTexts[1] = (EditText) findViewById(R.id.answerB_editText);
        answerEditTexts[2] = (EditText) findViewById(R.id.answerC_editText);
        answerEditTexts[3] = (EditText) findViewById(R.id.answerD_editText);

        if(question != null){
            questionTextView.setText(question.getQuestion());

            answerEditTexts[0].setText(question.getAnswers()[0].getAnswer());
            answerEditTexts[1].setText(question.getAnswers()[1].getAnswer());
            answerEditTexts[2].setText(question.getAnswers()[2].getAnswer());
            answerEditTexts[3].setText(question.getAnswers()[3].getAnswer());

            for(int i = 0; i< 4; ++i){

                if(question.getAnswers()[i].getId() == question.getAnswerId())
                    answerButtons[i].setChecked(true);
                else
                    answerButtons[i].setChecked(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v instanceof RadioButton){

            for(int i =0; i< 4; ++i)
                answerButtons[i].setChecked(false);

            ((RadioButton) v).setChecked(true);
        }
    }
}
