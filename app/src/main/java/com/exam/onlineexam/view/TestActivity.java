package com.exam.onlineexam.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.exam.onlineexam.R;
import com.exam.onlineexam.Utils;
import com.exam.onlineexam.adapter.QuestionsPagerAdapter;
import com.exam.onlineexam.model.Question;
import com.exam.onlineexam.model.Result;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.exam.onlineexam.Constants.TEST_QUESTIONS;
import static com.exam.onlineexam.Constants.TEST_SELECTED;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabQuestions;
    private ViewPager vpQuestionis;
    private QuestionsPagerAdapter adapter;
    private ImageButton btnBack;
    private TextView txtTimer, txtTestName;
    private CountDownTimer timer;
    private Button btnSubmit;
    private FirebaseAuth auth;
    private int ansCount, attemptedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        auth = FirebaseAuth.getInstance();

        btnBack = findViewById(R.id.btn_back);
        txtTestName = findViewById(R.id.lbl_title);
        txtTimer = findViewById(R.id.txt_time);
        btnSubmit = findViewById(R.id.btn_submit);
        tabQuestions = findViewById(R.id.tab_questions);
        vpQuestionis = findViewById(R.id.vp_questions);

        txtTestName.setText(getIntent().getStringExtra(TEST_SELECTED));

        vpQuestionis.setOffscreenPageLimit(3);
        adapter = new QuestionsPagerAdapter(getSupportFragmentManager(),
                (ArrayList<Question>) getIntent().getSerializableExtra(TEST_QUESTIONS));
        vpQuestionis.setAdapter(adapter);
        vpQuestionis.setCurrentItem(0);
        tabQuestions.setupWithViewPager(vpQuestionis);
        setTabs();

        btnBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        timer = new CountDownTimer((1000 * 60 * 30 + 1000), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtTimer.setText(String.format("%02d", ((millisUntilFinished / 1000) / 60)) + ":" +
                        String.format("%02d", ((millisUntilFinished / 1000) % 60)));
            }

            @Override
            public void onFinish() {
                txtTimer.setText("");
                onTestFinished();
            }
        }.start();
    }

    private void setTabs() {
        tabQuestions.getTabAt(0).setText("Q.1 - Q.10");
        tabQuestions.getTabAt(0).setTag("Q.1 - Q.10");
        tabQuestions.getTabAt(1).setText("Q.11 - Q.20");
        tabQuestions.getTabAt(1).setTag("Q.11 - Q.20");
        tabQuestions.getTabAt(2).setText("Q.21 - Q.30");
        tabQuestions.getTabAt(2).setTag("Q.21 - Q.30");
    }

    @Override
    public void onBackPressed() {
        if (adapter != null) {
            vpQuestionis.setAdapter(null);
            adapter = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        finish();
        overridePendingTransition(R.anim.no_anim, R.anim.slide_to_right);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_submit:
                onTestFinished();
                break;
        }
    }

    private void onTestFinished() {
        if (timer != null) {
            timer.cancel();
        }
        ansCount = ((QuestionsFragment) adapter.getItem(0)).getCorrectAnsCount();
        ansCount += ((QuestionsFragment) adapter.getItem(1)).getCorrectAnsCount();
        ansCount += ((QuestionsFragment) adapter.getItem(2)).getCorrectAnsCount();
        attemptedCount = ((QuestionsFragment) adapter.getItem(0)).getSolvedQuestionCount();
        attemptedCount += ((QuestionsFragment) adapter.getItem(1)).getSolvedQuestionCount();
        attemptedCount += ((QuestionsFragment) adapter.getItem(2)).getSolvedQuestionCount();
        new AlertDialog.Builder(TestActivity.this)
                .setTitle("Test Result:")
                .setMessage("Total Question: 30\nSolved Questions: " + attemptedCount + "\nCorrect Answers: " + ansCount)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        saveResult(ansCount, attemptedCount);
                        onBackPressed();
                    }
                }).setCancelable(false)
                .create().show();
    }

    void saveResult(int marks, int questionAttempted) {
        if (Utils.isConnectedToInternet(TestActivity.this)) {
            if (auth.getCurrentUser() != null) {
                String userId = auth.getCurrentUser().getUid();
                String date = Utils.getCurrentDate();
                Result result = Result.createResult(getIntent().getStringExtra(TEST_SELECTED), date ,marks,questionAttempted);
                DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference("Results/"+userId);
                String key = questionRef.push().getKey();
                if(key !=  null)
                    questionRef.child(key).setValue(result);
            }
        }
    }
}
