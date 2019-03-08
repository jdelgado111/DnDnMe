package com.example.janethdelgado.dndnme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

                tvUsername.setText(username);
                tvShortBio.setText(shortBio);
                tvLongBio.setText(longBio);
            }
        });
    }
}