package com.exam.onlineexam.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.exam.onlineexam.ProgressPopUp;
import com.exam.onlineexam.R;
import com.exam.onlineexam.Utils;
import com.exam.onlineexam.model.User;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword, edtConfirmPassword, edtName;
    private Button btnSignUp;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private ProgressPopUp popUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("User");
        btnSignUp = findViewById(R.id.btnSignUp);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isConnectedToInternet(SignUpActivity.this)) {
                    if (validate()) {
                        popUp = new ProgressPopUp(SignUpActivity.this);
                        popUp.showLoading();
                        auth.createUserWithEmailAndPassword(edtEmail.getText().toString().trim(),
                                edtPassword.getText().toString())
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            popUp.dismissLoading();
                                            Toast.makeText(SignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(edtName.getText().toString())
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            popUp.dismissLoading();
                                                            onBackPressed();
                                                        }
                                                    })
                                                    .addOnCanceledListener(SignUpActivity.this, new OnCanceledListener() {
                                                        @Override
                                                        public void onCanceled() {
                                                            popUp.dismissLoading();
                                                            onBackPressed();
                                                        }
                                                    })
                                                    .addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            popUp.dismissLoading();
                                                            onBackPressed();
                                                        }
                                                    });
                                        }
                                    }
                                })
                                .addOnCanceledListener(SignUpActivity.this, new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {
                                        popUp.dismissLoading();
                                    }
                                })
                                .addOnFailureListener(SignUpActivity.this, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        popUp.dismissLoading();
                                    }
                                });
                    }
                }
            }
        });
    }

    private boolean validate() {
        if (TextUtils.isEmpty(edtName.getText().toString())) {
            edtName.setError("Name is empty");
            edtName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            edtEmail.setError("Email is empty");
            edtEmail.requestFocus();
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString().trim()).matches()) {
            edtEmail.setError("Invalid email");
            edtEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            edtPassword.setError("Password is empty");
            edtPassword.requestFocus();
            return false;
        } else if (edtPassword.getText().toString().length() < 6) {
            edtPassword.setError("Minimum 6 character required");
            edtPassword.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(edtConfirmPassword.getText().toString())) {
            edtConfirmPassword.setError("Confirm Password is empty");
            edtConfirmPassword.requestFocus();
            return false;
        } else if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            edtConfirmPassword.setError("Password not matched");
            edtConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_anim, R.anim.slide_to_right);    }
}
