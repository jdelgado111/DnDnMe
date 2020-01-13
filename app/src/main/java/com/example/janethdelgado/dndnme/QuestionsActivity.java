package com.example.janethdelgado.dndnme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.janethdelgado.dndnme.Models.Profile;
import com.example.janethdelgado.dndnme.Models.Questions;
import com.example.janethdelgado.dndnme.Models.Stats;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;
import java.util.Random;

public class QuestionsActivity extends AppCompatActivity {

    private final String TAG = "QuestionsActivity";
    private final int QUESTIONS_PER_CATEGORY = 1;

    private TextView tvQuestion;
    private TextView tvQuestion2;
    private TextView tvDisagree;
    private TextView tvNeutral;
    private TextView tvAgree;
    private RadioGroup rgButtons;
    private Button btnSubmit;

    private BottomNavigationView bottomNavigationView;

    private double persStat1;
    private double persStat2;
    private double persStat3;
    private double persStat4;

    private double searchStat1;
    private double searchStat2;
    private double searchStat3;
    private double searchStat4;

    private double prefValue1;
    private double prefValue2;
    private double prefValue3;
    private double prefValue4;

    private String pref;

    private ParseUser user = ParseUser.getCurrentUser();
    private Stats stats;
    private Questions q;

    //private List<Questions> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvQuestion2 = findViewById(R.id.tvQuestion2);
        tvDisagree = findViewById(R.id.tvDisagree);
        tvNeutral = findViewById(R.id.tvNeutral);
        tvAgree = findViewById(R.id.tvAgree);

        rgButtons = findViewById(R.id.rgButtons);
        btnSubmit = findViewById(R.id.btnSubmit);

        tvQuestion2.setVisibility(View.INVISIBLE);

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

                //get first question randomly
                int random = getRandomNumber(0, questions.size() - 1);
                q = questions.get(random);
                String question = q.getQuestion();

                //display first question
                tvQuestion.setText(question);

                //remove first question from list
                questions.remove(random);


                //handle clicks
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
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation();

                // get selected radio button from radioGroup
                int selectedId = rgButtons.getCheckedRadioButtonId();

                //update stats of question
                addStats(q, selectedId);


                //set next question
                int i = getRandomNumber(0, questions.size());
                if (i < questions.size()) {
                    q = questions.get(i);
                    String ques = q.getQuestion();

                    if(tvQuestion.getVisibility() == View.VISIBLE) {
                        tvQuestion2.setText(ques);
                    }
                    else if(tvQuestion2.getVisibility() == View.VISIBLE) {
                        tvQuestion.setText(ques);
                    }

                    //remove used question from list
                    questions.remove(i);
                }
                else if (i == questions.size() && i != 0) {
                    i = 0;
                    q = questions.get(i);
                    String ques = q.getQuestion();

                    if(tvQuestion.getVisibility() == View.VISIBLE) {
                        tvQuestion2.setText(ques);
                    }
                    else if(tvQuestion2.getVisibility() == View.VISIBLE) {
                        tvQuestion.setText(ques);
                    }

                    //remove used question from list
                    questions.remove(i);
                }
                else {  //size == 0, all questions complete
                    String end = "Questionnaire Complete!";
                    tvQuestion.setText(end);

                    rgButtons.setVisibility(View.INVISIBLE);
                    tvDisagree.setVisibility(View.INVISIBLE);
                    tvNeutral.setVisibility(View.INVISIBLE);
                    tvAgree.setVisibility(View.INVISIBLE);
                    btnSubmit.setVisibility(View.INVISIBLE);

                    //finalize stats (averages, set them) at the end
                    finalizeStats();

                }
            }
        });
    }

    private int getRandomNumber(int minNumber, int maxNumber){
        Random rand = new Random();
        return rand.nextInt((maxNumber - minNumber) + 1) + minNumber;
    }

    private void addStats(Questions q, int selection) {
        double multiplier = 0.0;

        switch(selection){
            case R.id.rBtn1:
                multiplier = -2.0;
                break;
            case R.id.rBtn2:
                multiplier = -1.5;
                break;
            case R.id.rBtn3:
                multiplier = 1;
                break;
            case R.id.rBtn4:
                multiplier = 1.5;
                break;
            case R.id.rBtn5:
                multiplier = 2.0;
                break;
        }

        //get weight of question
        int weight = q.getWeight();

        //assign value
        double value = multiplier * weight;

        //get category
        String cat = q.getCategory();

        switch(cat) {
            case "Rules Personal":
                persStat1 += value;
                break;
            case "Openness Personal":
                persStat2 += value;
                break;
            case "Experience Personal":
                persStat3 += value;
                break;
            case "Creativity Personal":
                persStat4 += value;
                break;
            case "Rules Search":
                searchStat1 += value;
                break;
            case "Openness Search":
                searchStat2 += value;
                break;
            case "Experience Search":
                searchStat3 += value;
                break;
            case "Creativity Search":
                searchStat4 += value;
                break;
            case "Rules Preference":
                prefValue1 = value;
                break;
            case "Openness Preference":
                prefValue2 = value;
                break;
            case "Experience Preference":
                prefValue3 = value;
                break;
            case "Creativity Preference":
                prefValue4 = value;
                break;
        }
    }

    private void finalizeStats() {
        //calculate averages
        persStat1 /= QUESTIONS_PER_CATEGORY;
        persStat2 /= QUESTIONS_PER_CATEGORY;
        persStat3 /= QUESTIONS_PER_CATEGORY;
        persStat4 /= QUESTIONS_PER_CATEGORY;

        searchStat1 /= QUESTIONS_PER_CATEGORY;
        searchStat2 /= QUESTIONS_PER_CATEGORY;
        searchStat3 /= QUESTIONS_PER_CATEGORY;
        searchStat4 /= QUESTIONS_PER_CATEGORY;

        //find preference category (highest value)
        double[] tempPref = {prefValue1, prefValue2, prefValue3, prefValue4};
        double max = tempPref[0];
        for (int i = 1; i < tempPref.length; i++) {
            if (tempPref[i] >= max) {
                max = tempPref[i];
            }
        }

        if (max == prefValue1) {
            pref = "Rules";
        }
        else if (max == prefValue2) {
            pref = "Openness";
        }
        else if (max == prefValue3) {
            pref = "Experience";
        }
        else if (max == prefValue4) {
            pref = "Creativity";
        }

        //get user's stats
        ParseQuery<Stats> query = ParseQuery.getQuery(Stats.class);
        query.include(Profile.KEY_USER);
        query.whereEqualTo(Profile.KEY_USER, user);
        query.findInBackground(new FindCallback<Stats>() {
            @Override
            public void done(List<Stats> stat, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }

                stats = stat.get(0);

                //set user stats
                stats.setPStat1(persStat1);
                stats.setPStat2(persStat2);
                stats.setPStat3(persStat3);
                stats.setPStat4(persStat4);

                stats.setSStat1(searchStat1);
                stats.setSStat2(searchStat2);
                stats.setSStat3(searchStat3);
                stats.setSStat4(searchStat4);

                stats.setPreference(pref);

                //save user stats to Parse
                stats.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving");
                            e.printStackTrace();
                            return;
                        }

                        Log.d(TAG, "Success!");
                    }
                });
            }
        });
    }

    private void animation() {
        final Animation leftOutAnim = AnimationUtils.loadAnimation(this, R.anim.left_out);
        final Animation rightInAnim = AnimationUtils.loadAnimation(this, R.anim.right_in);

        leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // this method is called when the animation first starts
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // this method is called when the animation is finished playing

                if(tvQuestion.getVisibility() == View.INVISIBLE) {
                    tvQuestion2.setVisibility(View.INVISIBLE);
                    tvQuestion.setVisibility(View.VISIBLE);
                    tvQuestion.startAnimation(rightInAnim);
                }
                else if(tvQuestion2.getVisibility() == View.INVISIBLE) {
                    tvQuestion.setVisibility(View.INVISIBLE);
                    tvQuestion2.setVisibility(View.VISIBLE);
                    tvQuestion2.startAnimation(rightInAnim);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // don't need to worry about this method
            }
        });

        tvQuestion.startAnimation(leftOutAnim);
    }
}