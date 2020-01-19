package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.janethdelgado.dndnme.Models.Profile;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

//Profile Activity acts as default Main Activity
public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvShortBio;
    private TextView tvLongBio;
    private Button btnEdit;

    private Profile userProfile;
    private ParseUser user;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvUsername = findViewById(R.id.tvUsername);
        tvShortBio = findViewById(R.id.tvShortBio);
        tvLongBio = findViewById(R.id.tvLongBio);
        btnEdit = findViewById(R.id.btnEdit);

        //check if current user is owner of this profile
        ParseUser possibleUser = getIntent().getParcelableExtra("user");

        if (possibleUser != null){
            user = possibleUser;
        }
        else {
            user = ParseUser.getCurrentUser();
        }

        //hide the edit button if the profile is being viewed by anyone other than this profile's user
        if(user == ParseUser.getCurrentUser()) {
            btnEdit.setVisibility(View.VISIBLE);
        }
        else {
            btnEdit.setVisibility(View.INVISIBLE);
        }

        //get Profile data from server
        //and fill in profile view
        fillProfile(user);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_profile:
                        break;
                    case R.id.action_matches:
                        Intent a = new Intent(ProfileActivity.this, MatchesActivity.class);
                        startActivity(a);
                        break;
                    case R.id.action_question:
                        Intent b = new Intent(ProfileActivity.this, QuestionsActivity.class);
                        startActivity(b);
                        break;
                    case R.id.action_logout:
                        Intent c = new Intent(ProfileActivity.this, LogoutActivity.class);
                        startActivity(c);
                        break;
                }
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_profile);


        //navigate to edit profile activity
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                ProfileActivity.this.startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) { // this 100 needs to match the 100 we used when we called startActivityForResult
            recreate();
            Log.d(TAG, "Returned to ProfileActivity");
        }
    }

    private void fillProfile(final ParseUser user) {
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
                    Glide.with(ProfileActivity.this).load(Uri.parse(image.getUrl())).into(ivProfileImage);
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

        Log.d(TAG, "Profile data filled successfully");
    }
}