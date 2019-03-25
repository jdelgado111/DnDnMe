package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseUser;

public class LogoutActivity extends AppCompatActivity {
    private Button btnLogout;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
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
                        //Toast.makeText(LogoutActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(LogoutActivity.this, ProfileActivity.class);
                        startActivity(a);
                        break;
                    case R.id.action_matches:
                        Toast.makeText(LogoutActivity.this, "Matches", Toast.LENGTH_SHORT).show();
                        Intent b = new Intent(LogoutActivity.this, MatchesActivity.class);
                        startActivity(b);
                        break;
                    case R.id.action_question:
                        //Toast.makeText(LogoutActivity.this, "Questions", Toast.LENGTH_SHORT).show();
                        Intent c = new Intent(LogoutActivity.this, QuestionsActivity.class);
                        startActivity(c);
                        break;
                    case R.id.action_logout:
                        Toast.makeText(LogoutActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_logout);
    }
}
