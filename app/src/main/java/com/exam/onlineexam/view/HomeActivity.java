package com.exam.onlineexam.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.exam.onlineexam.ProgressPopUp;
import com.exam.onlineexam.R;
import com.exam.onlineexam.Utils;
import com.exam.onlineexam.adapter.TestAdapter;
import com.exam.onlineexam.model.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.exam.onlineexam.Constants.LOGGED_IN;
import static com.exam.onlineexam.Constants.ONLINE_EXAM;
import static com.exam.onlineexam.Constants.TEST_QUESTIONS;
import static com.exam.onlineexam.Constants.TEST_SELECTED;

public class HomeActivity extends AppCompatActivity {

    TextView txtLogout, txtEmail;
    RecyclerView rvTests;
    private SharedPreferences pref;
    private FirebaseAuth auth;
    private ProgressPopUp popUp;
    private TestAdapter adapter;
    private Button btnRetry;
    private AlertDialog dialog;
    private DrawerLayout drawerLayout;
    private ImageButton btnBack;
    private ConstraintLayout clNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        pref = getSharedPreferences(ONLINE_EXAM, Context.MODE_PRIVATE);

        txtEmail = findViewById(R.id.txt_email);
        txtLogout = findViewById(R.id.txt_logout);
        btnBack = findViewById(R.id.btn_back);
        drawerLayout = findViewById(R.id.drawer);
        clNavigation = findViewById(R.id.cl_navigation);
        btnRetry = findViewById(R.id.btn_retry);
        rvTests = findViewById(R.id.rv_tests);

        if (auth.getCurrentUser() != null) {
            txtEmail.setText(auth.getCurrentUser().getEmail());
            FirebaseDatabase.getInstance().getReference("Admin")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> admins = new ArrayList<String>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                admins.add(dataSnapshot1.getValue(String.class));
                            }
                            if(admins.contains(auth.getCurrentUser().getEmail())){
                                clNavigation.setVisibility(View.VISIBLE);
                                btnBack.setVisibility(View.VISIBLE);
                                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                            } else {
                                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                clNavigation.setVisibility(View.GONE);
                                btnBack.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            clNavigation.setVisibility(View.GONE);
                            btnBack.setVisibility(View.GONE);
                            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        }
                    });
        }else {
            clNavigation.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        setTests();

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                pref.edit().putBoolean(LOGGED_IN, false).commit();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.no_anim);
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTests();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void setTests() {
        if (Utils.isConnectedToInternet(HomeActivity.this)) {
            btnRetry.setVisibility(View.GONE);
            popUp = new ProgressPopUp(HomeActivity.this);
            popUp.showLoading();
            FirebaseDatabase.getInstance().getReference("TestNames")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList tests = new ArrayList();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                tests.add(dataSnapshot1.getValue());
                            }
                            popUp.dismissLoading();
                            adapter = new TestAdapter(HomeActivity.this, tests,
                                    new TestAdapter.OnTestStartClickListener() {
                                        @Override
                                        public void onStartClick(View view, int position, final String testName) {
                                            if (dialog == null) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                                                        .setTitle("Test Instructions")
                                                        .setMessage(R.string.test_rules)
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                                if (dialog != null) {
                                                                    dialog.dismiss();
                                                                    dialog = null;
                                                                }
                                                                getQuestions(testName);
                                                            }
                                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                                if (dialog != null) {
                                                                    dialog.dismiss();
                                                                    dialog = null;
                                                                }
                                                            }
                                                        }).setCancelable(false);
                                                dialog = builder.create();
                                                dialog.show();
                                            }
                                        }
                                    });
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(HomeActivity.this, R.anim.layout_animation_from_bottom);
                            rvTests.setAdapter(adapter);
                            rvTests.setLayoutAnimation(animation);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            popUp.dismissLoading();
                        }
                    });
        } else btnRetry.setVisibility(View.VISIBLE);
    }

    void addQuestion() {
        if (Utils.isConnectedToInternet(HomeActivity.this)) {
            for (int i = 1; i <= 30; i++) {
                Question question = Question.createQuestion("Question: " + i, "option 1", "option 2",
                        "option 3", "option 4", "" + (i % 4));
                DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference("Questions/C Programming");
                String key = questionRef.push().getKey();
                questionRef.child(key).setValue(question);
            }
        }
    }

    private void getQuestions(final String testName) {
        if (Utils.isConnectedToInternet(HomeActivity.this)) {
            popUp = new ProgressPopUp(HomeActivity.this);
            popUp.showLoading();
            FirebaseDatabase.getInstance().getReference("Questions/" +testName)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            popUp.dismissLoading();
                            ArrayList<Question> tests = new ArrayList<Question>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                tests.add(dataSnapshot1.getValue(Question.class));
                            }
                            Collections.shuffle(tests);
                            ArrayList<Question> tests1 = new ArrayList<Question>();
                            if(tests.size()>=30) {
                                for (int i = 0; i < 30; i++) {
                                    tests1.add(tests.get(i));
                                }
                                startActivity(new Intent(HomeActivity.this, TestActivity.class)
                                        .putExtra(TEST_SELECTED, testName)
                                        .putExtra(TEST_QUESTIONS, tests1));
                                overridePendingTransition(R.anim.slide_from_right, R.anim.no_anim);
                            }else {
                                Toast.makeText(HomeActivity.this, "Data not available", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            popUp.dismissLoading();
                        }
                    });
        }
    }

}
