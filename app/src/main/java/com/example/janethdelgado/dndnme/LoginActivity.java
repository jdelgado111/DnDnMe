package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //current signed in user is persisted across app restarts
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null)
            goMainActivity();

        etUsername = findViewById(R.id.tvUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                login(username, password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                //add proper error handling here
                if (e != null) {
                    Log.e(TAG, "Issue with login");
                    e.printStackTrace();
                    return;
                }

                // navigate to new activity if the user has signed in properly
                goMainActivity();
            }
        });
    }

    private void signUp() {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    private void goMainActivity() {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);

        //prevents user going back to this screen after logging in
        finish();
    }
}