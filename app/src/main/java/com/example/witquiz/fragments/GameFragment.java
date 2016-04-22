package com.example.witquiz.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.witquiz.R;
import com.example.witquiz.activities.MainActivity;
import com.example.witquiz.databasemanager.DatabaseManager;
import com.example.witquiz.entities.Answer;
import com.example.witquiz.entities.Question;

public class GameFragment extends Fragment {

    private TextView questionTextView;
    private ToggleButton[] answerButtons;
    TextView questionNrTextView;
    private Question[] questions;
    int currentQuestionIndex = 0;
    private int checkedIndex = -1;
    Button confirmButton;
    String categoryName;

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_game,
                container, false);

        if(savedInstanceState!= null){
            questions = (Question[]) savedInstanceState.getParcelableArray("questions");
            currentQuestionIndex = savedInstanceState.getInt("questionIndex");
            checkedIndex = savedInstanceState.getInt("checked");
            categoryName = savedInstanceState.getString("categoryName");
        }
        else{
            Parcelable[] parcelables =  this.getArguments().getParcelableArray("questions");
            questions = new Question[parcelables.length];

            System.arraycopy(parcelables, 0, questions, 0, parcelables.length);

            checkedIndex = -1;
            categoryName = this.getArguments().getString("categoryName");
        }

        loadControls(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        final Answer[] answers = questions[currentQuestionIndex].getAnswers();

        for(int i = 0; i<4 ;++i){

            ToggleButton button = answerButtons[i];

            if(button.isChecked() && checkedIndex >= 0)
                markAnswers(answers[i].getId(), i);

            if(checkedIndex >= 0)
                button.setEnabled(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArray("questions", questions);
        outState.putInt("questionIndex", currentQuestionIndex);
        outState.putInt("checked", checkedIndex);
        outState.putString("categoryName", categoryName);

        super.onSaveInstanceState(outState);
    }

    private void loadControls(View view){

        TextView userNameTextView = (TextView) view.findViewById(R.id.username_name_textView);
        String userName = this.getActivity().getSharedPreferences(MainActivity.SHARED_PREFERENCES, 0).getString("CurrentUser", "");

        userNameTextView.setText(String.format(getResources().getString(R.string.username), userName));

        TextView categoryTextView = (TextView) view.findViewById(R.id.category_name_textView);
        categoryTextView.setText(categoryName);

        questionNrTextView = (TextView) view.findViewById(R.id.question_number_textView);

        questionTextView = (TextView) view.findViewById(R.id.question_textView);

        answerButtons = new ToggleButton[4];

        answerButtons[0] = (ToggleButton) view.findViewById(R.id.a_answer_button);
        answerButtons[1] = (ToggleButton) view.findViewById(R.id.b_answer_button);
        answerButtons[2] = (ToggleButton) view.findViewById(R.id.c_answer_button);
        answerButtons[3] = (ToggleButton) view.findViewById(R.id.d_answer_button);

        loadControlsText();

        for(int i = 0; i<4 ;++i){

            ToggleButton button = answerButtons[i];

            button.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    for(ToggleButton button : answerButtons){
                        if(button != v)
                            button.setChecked(false);
                    }

                }

            });
        }


        confirmButton = (Button) view.findViewById(R.id.confirm_button);

        if(checkedIndex >= 0)
            confirmButton.setText(R.string.next);
        else
            confirmButton.setText(R.string.confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Question question = questions[currentQuestionIndex];
                Answer[] answers = question.getAnswers();

                if(checkedIndex < 0){

                    int checkedAnswerId = 0;
                    int answerIndex = 0;

                    while(answerIndex < 4){

                        ToggleButton button = answerButtons[answerIndex];

                        if(button.isChecked()){
                            checkedAnswerId = answers[answerIndex].getId();
                            break;
                        }

                        ++answerIndex;
                    }

                    if(checkedAnswerId > 0){

                        checkedIndex = answerIndex;

                        for(ToggleButton button : answerButtons)
                            button.setEnabled(false);

                        ((Button)v).setText(R.string.next);

                        if(! markAnswers(checkedAnswerId, answerIndex))
                            return;

                    }
                    else{
                        Toast.makeText(GameFragment.this.getActivity(), "Żadna odpowiedź nie została zaznaczona.", Toast.LENGTH_SHORT).show();
                    }

                }

                else{
                    ++currentQuestionIndex;

                    if(currentQuestionIndex >= questions.length){

                        --currentQuestionIndex;

                        endOfGame(answers[checkedIndex].getId() == question.getAnswerId());
                        return;
                    }


                    checkedIndex = -1;
                    ((Button)v).setText(R.string.confirm);

                    for(ToggleButton button : answerButtons)
                        button.setEnabled(true);

                    for(ToggleButton button : answerButtons){
                        button.setChecked(false);
                        button.setBackground(getResources().getDrawable(R.drawable.question_button_selector));
                    }

                    loadControlsText();
                }

            }
        });

    }

    private void loadControlsText(){

        questionNrTextView.setText(String.format(getResources().getString(R.string.question_nr), currentQuestionIndex +1));

        Question question = questions[currentQuestionIndex];
        Answer[] answers = question.getAnswers();

        questionTextView.setText(question.getQuestion());

        String[] answersPrefs = this.getResources().getStringArray(R.array.answers_array);

        for(int i = 0; i<4 ;++i){

            ToggleButton button = answerButtons[i];

            button.setTextOn(String.format(answersPrefs[i], answers[i].getAnswer()));
            button.setTextOff(String.format(answersPrefs[i], answers[i].getAnswer()));
            button.setText(String.format(answersPrefs[i], answers[i].getAnswer()));
        }
    }

    private boolean markAnswers(int checkedAnswerId, int answerIndex){
        Question question = questions[currentQuestionIndex];
        Answer[] answers = question.getAnswers();

        if(checkedAnswerId == question.getAnswerId()){
            answerButtons[answerIndex]
                    .setBackground(getResources().getDrawable(R.drawable.question_button_ok_selector));

            if(currentQuestionIndex == questions.length -1)
                endOfGame(true);

            return true;
        }
        else{

            answerButtons[answerIndex]
                    .setBackground(getResources().getDrawable(R.drawable.question_button_wrong_selector));

            for(int i = 0; i<4; ++i){

                if(answers[i].getId() == question.getAnswerId()){

                    answerButtons[i]
                            .setBackground(getResources().getDrawable(R.drawable.question_button_ok_selector));

                    break;
                }
            }

            return endOfGame(false);
        }

    }

    private boolean endOfGame(final boolean success){

        String endText= getResources().getString(R.string.game_end);

        if(success){
            endText = String.format(endText, getResources().getString(R.string.won));
            confirmButton.setText(R.string.won);

        } else{
            endText = String.format(endText, getResources().getString(R.string.lost));
            confirmButton.setText(R.string.lost);

        }

        for(ToggleButton button : answerButtons)
            button.setEnabled(false);

        confirmButton.setEnabled(false);

        TextView endTextView = (TextView) getActivity().getLayoutInflater().inflate(R.layout.end_game_dialog, null);
        endTextView.setText(endText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        builder.setView(endTextView)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameFragment.this.getActivity().finish();

                        String userName = GameFragment.this.getActivity()
                                .getSharedPreferences(MainActivity.SHARED_PREFERENCES, 0).getString("CurrentUser", "");

                        if(success)
                            currentQuestionIndex++;

                        DatabaseManager.saveHighScore(userName, currentQuestionIndex);

                    }

                });

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        return success;
    }
}
