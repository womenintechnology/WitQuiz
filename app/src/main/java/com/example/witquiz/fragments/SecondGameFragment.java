package com.example.witquiz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.witquiz.R;

public class SecondGameFragment extends Fragment {

    private int allQuestions;
    private int currentQuestion;

    private TextView answeredQuestionsTextView;
    private TextView questionsLeftTextView;

    public SecondGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_second_game, container, false);

        answeredQuestionsTextView = (TextView) rootView.findViewById(R.id.answered_questions_textView);
        questionsLeftTextView = (TextView) rootView.findViewById(R.id.questions_left_textView);

        answeredQuestionsTextView
                .setText(String.format(getResources().getString(R.string.questions_answered), currentQuestion));

        questionsLeftTextView
                .setText(String.format(getResources().getString(R.string.questions_left), allQuestions - currentQuestion));

        return rootView;
    }

    public void setSummary(int allQuestions, int currentQuestion){
        this.allQuestions = allQuestions;
        this.currentQuestion = currentQuestion;

        if(this.isAdded()
                && answeredQuestionsTextView != null
                && questionsLeftTextView != null){

            answeredQuestionsTextView
                    .setText(String.format(getResources().getString(R.string.questions_answered), currentQuestion));

            questionsLeftTextView
                    .setText(String.format(getResources().getString(R.string.questions_left), allQuestions - currentQuestion));
        }
    }

}
