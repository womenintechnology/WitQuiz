package com.example.witquiz.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.witquiz.R;
import com.example.witquiz.databasemanager.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES = "WIT_PREFERENCES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadControls();

        setupDbEnv();
    }

    private void loadControls() {

        Button startButton = (Button) findViewById(R.id.start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText userNameEditText = (EditText) findViewById(R.id.userName_editText);
                String userName = userNameEditText.getText().toString();

                if(userName.isEmpty()){
                    Toast.makeText(MainActivity.this, getString(R.string.set_username), Toast.LENGTH_LONG).show();
                    return;
                }

                SharedPreferences shared = getSharedPreferences(SHARED_PREFERENCES, 0);
                shared.edit()
                        .putString("CurrentUser", userName)
                        .apply();

                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        DatabaseHelper.close();
        super.onDestroy();
    }

    private void setupDbEnv() {
        DatabaseHelper.setDatabaseManagerContext(this);
    }
}
