package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.janethdelgado.dndnme.Models.Profile;
import com.example.janethdelgado.dndnme.Models.Stats;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;
    private Button btnSignUp;

    private ParseFile defaultImage;

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

        //register user
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with sign up");
                    Toast.makeText(SignupActivity.this, "Invalid sign up, try again", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }

                //create default Profile for new User
                createProfile(user);

                //create default Stats for new User
                createStats(user);

                Log.d(TAG, "User has signed up successfully");

                // navigate to new activity if the user has signed up properly
                goMainActivity();
            }
        });
    }

    //create default profile and save to Parse
    private void createProfile(ParseUser parseUser){
        final Profile profile = new Profile();

        profile.setShortBio("");
        profile.setLongBio("");
        profile.setUser(parseUser);

        //default image taken from existing default user
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        query.getInBackground("wa6q24VD5V", new GetCallback<ParseObject>() {
            public void done(ParseObject searchProfile, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Couldn't retrieve default image");
                    e.printStackTrace();
                    return;
                }
                else {
                    defaultImage = searchProfile.getParseFile("profileImage");

                    if (defaultImage != null)
                        profile.setProfileImage(defaultImage);
                    else
                        Log.d(TAG, "Default image is null");
                }
            }
        });

        profile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving");
                    e.printStackTrace();
                    return;
                }

                Log.d(TAG, "Created and saved profile");
            }
        });
    }

    //create default stats and save to Parse
    private void createStats(ParseUser parseUser) {
        Stats stats = new Stats();

        stats.setUser(parseUser);

        //default values
        stats.setPStat1(1);
        stats.setPStat2(1);
        stats.setPStat3(1);
        stats.setPStat4(1);

        stats.setSStat1(1);
        stats.setSStat2(1);
        stats.setSStat3(1);
        stats.setSStat4(1);

        //default category preference
        stats.setPreference("Rules");

        stats.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving");
                    e.printStackTrace();
                    return;
                }

                Log.d(TAG, "Created and saved stats");
            }
        });
    }

    //launch the main activity
    private void goMainActivity() {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);

        //prevents user going back to this screen after signing up
        finish();
    }
}