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
    private TextView tvDisagree;
    private TextView tvNeutral;
    private TextView tvAgree;
    private RadioGroup rgButtons;
    //private RadioButton rBtn;
    private Button btnSubmit;

    private BottomNavigationView bottomNavigationView;

    private double persStat1;
    private double persStat2;
    private double persStat3;
    private double persStat4;
    private double persStat5;

    private double searchStat1;
    private double searchStat2;
    private double searchStat3;
    private double searchStat4;
    private double searchStat5;

    private double prefValue1;
    private double prefValue2;
    private double prefValue3;
    private double prefValue4;
    private double prefValue5;

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
        tvDisagree = findViewById(R.id.tvDisagree);
        tvNeutral = findViewById(R.id.tvNeutral);
        tvAgree = findViewById(R.id.tvAgree);

        rgButtons = findViewById(R.id.rgButtons);
        btnSubmit = findViewById(R.id.btnSubmit);

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
                    tvQuestion.setText(ques);

                    //remove used question from list
                    questions.remove(i);
                }
                else if (i == questions.size() && i != 0) {
                    i = 0;
                    q = questions.get(i);
                    String ques = q.getQuestion();
                    tvQuestion.setText(ques);

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
            case "Category 1 Personal":
                persStat1 += value;
                break;
            case "Category 2 Personal":
                persStat2 += value;
                break;
            case "Category 3 Personal":
                persStat3 += value;
                break;
            case "Category 4 Personal":
                persStat4 += value;
                break;
            case "Category 5 Personal":
                persStat5 += value;
                break;
            case "Category 1 Search":
                searchStat1 += value;
                break;
            case "Category 2 Search":
                searchStat2 += value;
                break;
            case "Category 3 Search":
                searchStat3 += value;
                break;
            case "Category 4 Search":
                searchStat4 += value;
                break;
            case "Category 5 Search":
                searchStat5 += value;
                break;
            case "Preference 1":
                prefValue1 += value;
                break;
            case "Preference 2":
                prefValue2 += value;
                break;
            case "Preference 3":
                prefValue3 += value;
                break;
            case "Preference 4":
                prefValue4 += value;
                break;
            case "Preference 5":
                prefValue5 += value;
                break;
        }
    }

    private void finalizeStats() {
        //calculate averages
        persStat1 /= QUESTIONS_PER_CATEGORY;
        persStat2 /= QUESTIONS_PER_CATEGORY;
        persStat3 /= QUESTIONS_PER_CATEGORY;
        persStat4 /= QUESTIONS_PER_CATEGORY;
        persStat5 /= QUESTIONS_PER_CATEGORY;

        searchStat1 /= QUESTIONS_PER_CATEGORY;
        searchStat2 /= QUESTIONS_PER_CATEGORY;
        searchStat3 /= QUESTIONS_PER_CATEGORY;
        searchStat4 /= QUESTIONS_PER_CATEGORY;
        searchStat5 /= QUESTIONS_PER_CATEGORY;

        prefValue1 /= QUESTIONS_PER_CATEGORY;
        prefValue2 /= QUESTIONS_PER_CATEGORY;
        prefValue3 /= QUESTIONS_PER_CATEGORY;
        prefValue4 /= QUESTIONS_PER_CATEGORY;
        prefValue5 /= QUESTIONS_PER_CATEGORY;

        //find preference category (highest value)
        double[] tempPref = {prefValue1, prefValue2, prefValue3, prefValue4, prefValue5};
        double max = tempPref[0];
        for (int i = 1; i < tempPref.length; i++) {
            if (tempPref[i] >= max) {
                max = tempPref[i];
            }
        }

        if (max == prefValue1) {
            pref = "Category 1";
        }
        else if (max == prefValue2) {
            pref = "Category 2";
        }
        else if (max == prefValue3) {
            pref = "Category 3";
        }
        else if (max == prefValue4) {
            pref = "Category 4";
        }
        else if (max == prefValue5) {
            pref = "Category 5";
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
                stats.setPStat5(persStat5);

                stats.setSStat1(searchStat1);
                stats.setSStat2(searchStat2);
                stats.setSStat3(searchStat3);
                stats.setSStat4(searchStat4);
                stats.setSStat5(searchStat5);

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
                tvQuestion.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // this method is called when the animation is finished playing
                tvQuestion.startAnimation(rightInAnim);

                tvQuestion.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // don't need to worry about this method
            }
        });

        tvQuestion.startAnimation(leftOutAnim);
    }
}