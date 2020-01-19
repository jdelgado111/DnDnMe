package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.example.janethdelgado.dndnme.Models.Profile;
import com.example.janethdelgado.dndnme.Models.Stats;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    private final String TAG = "MatchesActivity";
    private final int WIDEN_RANGE = 5;

    private RecyclerView rvProfiles;
    private ProfilesAdapter adapter;
    private List<Profile> mProfiles;

    private BottomNavigationView bottomNavigationView;

    private Stats stats;
    private String preference;
    private double maxRange;
    private double minRange;
    private double statsCheck;

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
        rvProfiles.setAdapter(adapter);

        //set the layout manager on the recycler view
        rvProfiles.setLayoutManager(new LinearLayoutManager(this));

        //determine what to match user with (preference)
        selectMatches();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_profile:
                        Intent a = new Intent(MatchesActivity.this, ProfileActivity.class);
                        startActivity(a);
                        break;
                    case R.id.action_matches:
                        break;
                    case R.id.action_question:
                        Intent b = new Intent(MatchesActivity.this, QuestionsActivity.class);
                        startActivity(b);
                        break;
                    case R.id.action_logout:
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

    private void selectMatches() {
        //get user's stats
        ParseQuery<Stats> statQuery = ParseQuery.getQuery(Stats.class);
        statQuery.include(Stats.KEY_USER);
        statQuery.whereEqualTo(Stats.KEY_USER, ParseUser.getCurrentUser());
        statQuery.findInBackground(new FindCallback<Stats>() {
            @Override
            public void done(List<Stats> returnedStats, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query - selectMatches");
                    e.printStackTrace();
                    return;
                }

                stats = returnedStats.get(0);

                //get user's preference (category)
                preference = stats.getPreference();

                switch (preference) {
                    case "Rules":
                        statsCheck = stats.getSStat1();
                        break;
                    case "Openness":
                        statsCheck = stats.getSStat2();
                        break;
                    case "Experience":
                        statsCheck = stats.getSStat3();
                        break;
                    case "Creativity":
                        statsCheck = stats.getSStat4();
                        break;
                    default:
                        statsCheck = stats.getSStat1();
                }

                //set range to search within
                maxRange = statsCheck + 1;
                minRange = statsCheck - 1;

                //find stats that match
                findMatches(preference);
            }
        });
    }

    private void findMatches(final String prefer) {
        ParseQuery<Stats> statsQuery = ParseQuery.getQuery(Stats.class);

        //set query parameters
        switch (prefer) {
            case "Rules":
                statsQuery.whereLessThanOrEqualTo(Stats.KEY_PSTAT1, maxRange);
                statsQuery.whereGreaterThanOrEqualTo(Stats.KEY_PSTAT1, minRange);
                statsQuery.addDescendingOrder(Stats.KEY_PSTAT1);
                break;
            case "Openness":
                statsQuery.whereLessThanOrEqualTo(Stats.KEY_PSTAT2, maxRange);
                statsQuery.whereGreaterThanOrEqualTo(Stats.KEY_PSTAT2, minRange);
                statsQuery.addDescendingOrder(Stats.KEY_PSTAT2);
                break;
            case "Experience":
                statsQuery.whereLessThanOrEqualTo(Stats.KEY_PSTAT3, maxRange);
                statsQuery.whereGreaterThanOrEqualTo(Stats.KEY_PSTAT3, minRange);
                statsQuery.addDescendingOrder(Stats.KEY_PSTAT3);
                break;
            case "Creativity":
                statsQuery.whereLessThanOrEqualTo(Stats.KEY_PSTAT4, maxRange);
                statsQuery.whereGreaterThanOrEqualTo(Stats.KEY_PSTAT4, minRange);
                statsQuery.addDescendingOrder(Stats.KEY_PSTAT4);
                break;
        }

        statsQuery.findInBackground(new FindCallback<Stats>() {
            @Override
            public void done(List<Stats> returnedStats, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query - findMatches");
                    e.printStackTrace();
                    return;
                }

                //if no matches, widen range and search again
                if(returnedStats.size() == 0) {
                    minRange -= WIDEN_RANGE;
                    maxRange += WIDEN_RANGE;
                    Log.d(TAG, "Searching again - Preference: " + prefer);
                    findMatches(prefer);
                }

                //get profiles tied to matching stats to display them
                getProfiles(returnedStats);
            }
        });
    }

    private void getProfiles(List<Stats> statsList) {
        ParseQuery<Profile> profileQuery = ParseQuery.getQuery(Profile.class);

        for (int i = 0; i < statsList.size(); i++) {
            Stats stat = statsList.get(i);
            ParseUser statUser = stat.getUser();

            String myId = stat.getUser().getObjectId();

            //don't match current user to themselves
            if (myId.equals(ParseUser.getCurrentUser().getObjectId()))
                continue;

            profileQuery.include(Profile.KEY_USER);
            profileQuery.whereEqualTo(Profile.KEY_USER, statUser);
            profileQuery.findInBackground(new FindCallback<Profile>() {
                @Override
                public void done(List<Profile> profiles, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Error with query - getProfiles");
                        e.printStackTrace();
                        return;
                    }

                    mProfiles.add(profiles.get(0));
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}