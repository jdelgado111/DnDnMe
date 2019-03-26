package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    private final String TAG = "PostsFragment";

    private RecyclerView rvProfiles;
    private ProfilesAdapter adapter;
    private List<Profile> mProfiles;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        rvProfiles = findViewById(R.id.rvProfiles);

        //create the data source
        mProfiles = new ArrayList<>();

        //create the adapter
        adapter = new ProfilesAdapter(this, mProfiles);

        //set the adapter on the recycler view
        //adapter tells the recycler view how to show the contents from the view
        rvProfiles.setAdapter(adapter);

        //set the layout manager on the recycler view
        //the layout manager is how you lay out the contents onto the screen
        rvProfiles.setLayoutManager(new LinearLayoutManager(this));

        queryProfiles();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_profile:
                        //Toast.makeText(MatchesActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(MatchesActivity.this, ProfileActivity.class);
                        startActivity(a);
                        break;
                    case R.id.action_matches:
                        //Toast.makeText(MatchesActivity.this, "Matches", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_question:
                        //Toast.makeText(MatchesActivity.this, "Questions", Toast.LENGTH_SHORT).show();
                        Intent b = new Intent(MatchesActivity.this, QuestionsActivity.class);
                        startActivity(b);
                        break;
                    case R.id.action_logout:
                        //Toast.makeText(MatchesActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                        Intent c = new Intent(MatchesActivity.this, LogoutActivity.class);
                        startActivity(c);
                        break;
                }
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_matches);
    }

    private void queryProfiles() {
        ParseQuery<Profile> postQuery = new ParseQuery<Profile>(Profile.class);
        postQuery.include(Profile.KEY_USER);
        postQuery.findInBackground(new FindCallback<Profile>() {
            @Override
            public void done(List<Profile> profiles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }

                //add retrieved List of profiles to our adapter-connected List
                mProfiles.addAll(profiles);
                adapter.notifyDataSetChanged();

                //log for debugging purposes
                /*for (int i = 0; i < profiles.size(); i++) {
                    Profile profile = profiles.get(i);
                    Log.d(TAG, "Profile: " + profile.getShortBio() +
                            ", username: " + profile.getUser().getUsername());
                }*/
            }
        });
    }
}
