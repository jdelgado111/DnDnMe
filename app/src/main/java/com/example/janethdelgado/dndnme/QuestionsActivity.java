package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Random;

public class QuestionsActivity extends AppCompatActivity {

    private final String TAG = "QuestionsActivity";

    private TextView tvQuestion;
    private RadioGroup rgButtons;
    private RadioButton rBtn;
    private Button btnSubmit;

    private BottomNavigationView bottomNavigationView;

    //private List<Questions> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        tvQuestion = findViewById(R.id.tvQuestion);

        //get list of questions
        //questions = new ArrayList<>();
        queryQuestions();

        //bottom navigation
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

        // set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_question);
    }

    private void queryQuestions() {
        ParseQuery<Questions> questionQuery = new ParseQuery<Questions>(Questions.class);
        //questionQuery.include(Profile.KEY_USER);
        questionQuery.findInBackground(new FindCallback<Questions>() {
            @Override
            public void done(List<Questions> questions, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }

                //questions.addAll(qs);
                Questions q = questions.get(0);
                String question = q.getQuestion();

                //set first question
                tvQuestion.setText(question);

                //remove first question from list
                questions.remove(0);

                buttonListener(questions);

                /*//log for debugging purposes
                for (int i = 0; i < questions.size(); i++) {
                    Questions ques = questions.get(i);
                    Log.d(TAG, "Question: " + ques.getQuestion());
                }*/
            }
        });
    }

    private void buttonListener(final List<Questions> questions) {
        rgButtons = findViewById(R.id.rgButtons);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = rgButtons.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rBtn = findViewById(selectedId);

                //Toast.makeText(QuestionsActivity.this, rBtn.getText(), Toast.LENGTH_SHORT).show();

                //TODO: update stats
                //calculate stats from answers given (sum + average??)
                //set state in Parse

                //go to next question
                int i = getRandomNumber(0, questions.size());
                if (i < questions.size()) {
                    Questions q = questions.get(i);
                    String ques = q.getQuestion();
                    tvQuestion.setText(ques);

                    //remove used question from list
                    questions.remove(i);
                }
                else if (i == questions.size() && i != 0) {
                    i = 0;
                    Questions q = questions.get(i);
                    String ques = q.getQuestion();
                    tvQuestion.setText(ques);

                    //remove used question from list
                    questions.remove(i);
                }
                else {
                    String end = "Questionnaire Complete!";
                    tvQuestion.setText(end);
                }
            }
        });
    }

    private int getRandomNumber(int minNumber, int maxNumber){
        Random rand = new Random();
        return rand.nextInt((maxNumber - minNumber) + 1) + minNumber;
    }
}