package com.example.witquiz.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.witquiz.R;

public class MenuActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        loadControls();
    }

    private void loadControls() {

        Button newGameButton = (Button) findViewById(R.id.new_game_button);
        Button highScoresButton = (Button) findViewById(R.id.best_scores_button);
        Button creatorButton = (Button) findViewById(R.id.creator_button);
        Button exitButton = (Button) findViewById(R.id.exit_button);

        creatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CreatorActivity.class);
                startActivity(intent);
            }
        });

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, NewGameActivity.class);
                startActivity(intent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }


}
