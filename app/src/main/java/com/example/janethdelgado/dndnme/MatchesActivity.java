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
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    private final String TAG = "MatchesActivity";

    private RecyclerView rvProfiles;
    private ProfilesAdapter adapter;
    private List<Profile> mProfiles;
    //private List<Profile> tempProfiles;

    private BottomNavigationView bottomNavigationView;

    //private ParseUser user = ParseUser.getCurrentUser();
    private Stats stats;
    private String preference;
    private double maxRange;
    private double minRange;
    private double statsCheck;

    //private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        rvProfiles = findViewById(R.id.rvProfiles);

        //create the data source
        mProfiles = new ArrayList<>();

        //create temp data source
        //tempProfiles = new ArrayList<>();

        //create the adapter
        adapter = new ProfilesAdapter(this, mProfiles);

        //set the adapter on the recycler view
        //adapter tells the recycler view how to show the contents from the view
        rvProfiles.setAdapter(adapter);

        //set the layout manager on the recycler view
        //the layout manager is how you lay out the contents onto the screen
        rvProfiles.setLayoutManager(new LinearLayoutManager(this));

        //determine what to match user with (preference)
        Log.d(TAG, "Calling selectMatches");
        selectMatches();

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

    private void selectMatches() {
        Log.d(TAG, "Inside selectMatches");

        //get stats of user
        //ParseQuery<Stats> statQuery = new ParseQuery<Stats>(Stats.class);
        ParseQuery<Stats> statQuery = ParseQuery.getQuery(Stats.class);
        //statQuery.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<Stats>() {
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

                Log.d(TAG, "In selectMatches - Preference: " + preference);

                switch (preference) {
                    case "Rules":
                        statsCheck = stats.getSStat1();
                        maxRange = statsCheck + 1;
                        minRange = statsCheck - 1;
                        break;
                    case "Openness":
                        statsCheck = stats.getSStat2();
                        maxRange = statsCheck + 1;
                        minRange = statsCheck - 1;
                        break;
                    case "Experience":
                        statsCheck = stats.getSStat3();
                        maxRange = statsCheck + 1;
                        minRange = statsCheck - 1;
                        break;
                    case "Creativity":
                        statsCheck = stats.getSStat4();
                        maxRange = statsCheck + 1;
                        minRange = statsCheck - 1;
                        break;
                    //case "Category 5":
                    //    statsCheck = stats.getSStat5();
                    //    maxRange = statsCheck + 1;
                    //    minRange = statsCheck - 1;
                    //    break;
                }

                //find stats that match
                Log.d(TAG, "Calling findMatches");
                findMatches(preference);
            }
        });
    }

    private void findMatches(final String prefer) {
        Log.d(TAG, "Inside findMatches");

        //String pref = prefer;

        ParseQuery<Stats> statsQuery = ParseQuery.getQuery(Stats.class);
        Log.d(TAG, "In findMatches - Preference: " + prefer);
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
            //case "Category 5":
            //    statsQuery.whereLessThanOrEqualTo(Stats.KEY_PSTAT5, maxRange);
            //    statsQuery.whereGreaterThanOrEqualTo(Stats.KEY_PSTAT5, minRange);
            //    statsQuery.addDescendingOrder(Stats.KEY_PSTAT5);
            //    break;
        }

        statsQuery.findInBackground(new FindCallback<Stats>() {
            @Override
            public void done(List<Stats> returnedStats, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query - findMatches");
                    e.printStackTrace();
                    return;
                }

                if(returnedStats.size() == 0) {
                    minRange -= 6;
                    maxRange +=6;
                    Log.d(TAG, "Searching again - Preference: " + prefer);
                    findMatches(prefer);
                }

                //get profiles tied to matching stats
                getProfiles(returnedStats);
            }
        });
    }

    private void getProfiles(List<Stats> statsList) {
        ParseQuery<Profile> profileQuery = ParseQuery.getQuery(Profile.class);
        Stats stat;

        for (int i = 0; i < statsList.size(); i++) {
            //position = i;
            stat = statsList.get(i);
            ParseUser statUser = stat.getUser();

            String myId = stat.getUser().getObjectId();

            if (myId.equals(ParseUser.getCurrentUser().getObjectId()))
                continue;

            Log.d(TAG, "myID: " + myId);

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

                    //Log.d(TAG, "mProfiles size: " + mProfiles.size() + " Count: " + position);

                    mProfiles.add(profiles.get(0));
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}