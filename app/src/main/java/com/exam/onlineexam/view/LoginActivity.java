package com.exam.onlineexam.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.exam.onlineexam.ProgressPopUp;
import com.exam.onlineexam.R;
import com.exam.onlineexam.Utils;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;

import static com.exam.onlineexam.Constants.LOGGED_IN;
import static com.exam.onlineexam.Constants.ONLINE_EXAM;

public class LoginActivity extends AppCompatActivity {

    private TextView txtForgotpass;
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private FirebaseAuth auth;
    private ProgressPopUp popUp;
    private FloatingActionButton fabNavigateSignUp;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getSharedPreferences(ONLINE_EXAM, Context.MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();

        txtForgotpass = findViewById(R.id.txt_forgot_password);
        btnLogin = findViewById(R.id.btnSignIn);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        fabNavigateSignUp = findViewById(R.id.fab_navigate_signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isConnectedToInternet(LoginActivity.this)) {
                    if (validate()) {
                        popUp = new ProgressPopUp(LoginActivity.this);
                        popUp.showLoading();
                        auth.signInWithEmailAndPassword(edtEmail.getText().toString().trim(),
                                edtPassword.getText().toString())
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        popUp.dismissLoading();
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                                        } else {
                                            pref.edit().putBoolean(LOGGED_IN, true).commit();
                                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                            finish();
                                            overridePendingTransition(R.anim.no_anim, R.anim.slide_to_left);
                                        }
                                    }
                                }).addOnCanceledListener(LoginActivity.this, new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                popUp.dismissLoading();
                            }
                        }).addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                popUp.dismissLoading();
                            }
                        });
                    }
                }
            }
        });

        fabNavigateSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.no_anim);
            }
        });

        txtForgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new ForgotPasswordDialog(LoginActivity.this);
                dialog.show();
            }
        });
    }

    private boolean validate(){
        if(TextUtils.isEmpty(edtEmail.getText().toString())){
            edtEmail.setError("Email is empty");
            edtEmail.requestFocus();
            return false;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString().trim()).matches()){
            edtEmail.setError("Invalid email");
            edtEmail.requestFocus();
            return false;
        }if(TextUtils.isEmpty(edtPassword.getText().toString())){
            edtEmail.setError("Password is empty");
            edtEmail.requestFocus();
            return false;
        }else return true;
    }
}
