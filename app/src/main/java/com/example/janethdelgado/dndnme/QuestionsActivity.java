package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionsActivity extends AppCompatActivity {

    private TextView tvQuestion;
    private RadioGroup rgButtons;
    private RadioButton rBtn;
    private Button btnSubmit;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        tvQuestion = findViewById(R.id.tvQuestion);
        //TODO: set text to fill text view from Parse

        buttonListener();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_profile:
                        //Toast.makeText(QuestionsActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(QuestionsActivity.this, ProfileActivity.class);
                        startActivity(a);
                        break;
                    case R.id.action_matches:
                        //Toast.makeText(QuestionsActivity.this, "Matches", Toast.LENGTH_SHORT).show();
                        Intent b = new Intent(QuestionsActivity.this, MatchesActivity.class);
                        startActivity(b);
                        break;
                    case R.id.action_question:
                        //Toast.makeText(QuestionsActivity.this, "Questions", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_logout:
                        //Toast.makeText(QuestionsActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                        Intent c = new Intent(QuestionsActivity.this, LogoutActivity.class);
                        startActivity(c);
                        break;
                }
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_question);
    }

    private void buttonListener() {
        rgButtons = findViewById(R.id.rgButtons);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = rgButtons.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rBtn = findViewById(selectedId);

                Toast.makeText(QuestionsActivity.this,
                        rBtn.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
