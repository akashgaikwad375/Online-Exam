package com.exam.onlineexam.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.exam.onlineexam.R;
import com.google.firebase.auth.FirebaseAuth;

import static com.exam.onlineexam.Constants.LOGGED_IN;
import static com.exam.onlineexam.Constants.ONLINE_EXAM;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth= FirebaseAuth.getInstance();
        pref = getSharedPreferences(ONLINE_EXAM, Context.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(pref.getBoolean(LOGGED_IN, false)){
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    SplashActivity.this.finish();
                    overridePendingTransition(R.anim.no_anim, R.anim.slide_to_left);
                }else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    SplashActivity.this.finish();
                    overridePendingTransition(R.anim.no_anim, R.anim.slide_to_left);
                }
            }
        }, 2000);
    }
}
