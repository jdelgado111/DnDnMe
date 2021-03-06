package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;

public class LogoutActivity extends AppCompatActivity {
    private static final String TAG = "LogoutActivity";

    private Button btnLogout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        btnLogout = findViewById(R.id.btnLogout);

        //log user out on Parse and navigate to login activity
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();

                Log.d(TAG, "User has logged out successfully");

                Intent i = new Intent(LogoutActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_profile:
                        Intent a = new Intent(LogoutActivity.this, ProfileActivity.class);
                        startActivity(a);
                        break;
                    case R.id.action_matches:
                        Intent b = new Intent(LogoutActivity.this, MatchesActivity.class);
                        startActivity(b);
                        break;
                    case R.id.action_question:
                        Intent c = new Intent(LogoutActivity.this, QuestionsActivity.class);
                        startActivity(c);
                        break;
                    case R.id.action_logout:
                        break;
                }
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_logout);
    }
}