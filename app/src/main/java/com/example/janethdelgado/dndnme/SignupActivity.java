package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.tvUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                signUp(username, password, email);
            }
        });
    }

    private void signUp(String username, String password, String email) {
        // Create the ParseUser
        final ParseUser user = new ParseUser();

        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with sign up");
                    e.printStackTrace();
                    return;
                }

                //create default Profile for new User
                createProfile(user);

                //create default Stats for new User
                createStats(user);

                // navigate to new activity if the user has signed up properly
                goMainActivity();
            }
        });
    }

    //create blank profile
    private void createProfile(ParseUser parseUser){
        Profile profile = new Profile();

        profile.setShortBio("");
        profile.setLongBio("");
        profile.setUser(parseUser);

        profile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving");
                    e.printStackTrace();
                    return;
                }

                Log.d(TAG, "Success!");
            }
        });
    }

    //TODO: update default category
    //create default stats
    private void createStats(ParseUser parseUser) {
        Stats stats = new Stats();

        stats.setUser(parseUser);

        stats.setPStat1(1);
        stats.setPStat2(1);
        stats.setPStat3(1);
        stats.setPStat4(1);
        stats.setPStat5(1);

        stats.setSStat1(1);
        stats.setSStat2(1);
        stats.setSStat3(1);
        stats.setSStat4(1);
        stats.setSStat5(1);

        stats.setPreference("Category 1");

        stats.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving");
                    e.printStackTrace();
                    return;
                }

                Log.d(TAG, "Success!");
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);

        //prevents user going back to this screen after signing up
        finish();
    }
}
