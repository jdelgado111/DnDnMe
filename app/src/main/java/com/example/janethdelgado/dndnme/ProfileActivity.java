package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvShortBio;
    private TextView tvLongBio;

    private Profile userProfile;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvUsername = findViewById(R.id.tvUsername);
        tvShortBio = findViewById(R.id.tvShortBio);
        tvLongBio = findViewById(R.id.tvLongBio);

        //get current user
        final ParseUser user = ParseUser.getCurrentUser();

        //get Profile data from server
        //and fill in profile view
        fillProfile(user);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_profile:
                        //Toast.makeText(ProfileActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_matches:
                        //Toast.makeText(ProfileActivity.this, "Matches", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(ProfileActivity.this, MatchesActivity.class);
                        startActivity(a);
                        break;
                    case R.id.action_question:
                        //Toast.makeText(ProfileActivity.this, "Questions", Toast.LENGTH_SHORT).show();
                        Intent b = new Intent(ProfileActivity.this, QuestionsActivity.class);
                        startActivity(b);
                        break;
                    case R.id.action_logout:
                        //Toast.makeText(ProfileActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                        Intent c = new Intent(ProfileActivity.this, LogoutActivity.class);
                        startActivity(c);
                        break;
                }
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_profile);
    }

    private void fillProfile(final ParseUser user) {
        Log.d(TAG, "in getProfile");
        ParseQuery<Profile> query = ParseQuery.getQuery(Profile.class);
        query.include(Profile.KEY_USER);
        query.whereEqualTo(Profile.KEY_USER, user);
        query.findInBackground(new FindCallback<Profile>() {
            @Override
            public void done(List<Profile> profiles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }

                userProfile = profiles.get(0);

                ParseFile image = userProfile.getProfileImage();
                String username = user.getUsername();
                String shortBio = userProfile.getShortBio();
                String longBio = userProfile.getLongBio();

                //populate Views
                if(image != null) {
                    Glide.with(ProfileActivity.this).load(image.getUrl()).into(ivProfileImage);
                }

                if (username != null && !(username.equals("")))
                    tvUsername.setText("@" + username);
                else
                    tvUsername.setText("@Default");

                if (shortBio != null && !(shortBio.equals("")))
                    tvShortBio.setText(shortBio);
                else
                    tvShortBio.setText("This is the default text for the short bio.");

                if (longBio != null && !(longBio.equals("")))
                    tvLongBio.setText(longBio);
                else
                    tvLongBio.setText("This is the default text for the long bio.");
            }
        });
    }
}