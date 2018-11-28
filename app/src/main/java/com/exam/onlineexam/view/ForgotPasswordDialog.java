package com.exam.onlineexam.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.exam.onlineexam.R;
import com.exam.onlineexam.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordDialog extends Dialog implements View.OnClickListener {

    Context context;
    private FirebaseAuth mAuth;
    private EditText edtEmail;

    public ForgotPasswordDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.edt_enter_email);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(true);
        show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                dismiss();
                if (Utils.isConnectedToInternet((LoginActivity)context)) {
                    if (validate()) {
                        mAuth.sendPasswordResetEmail(edtEmail.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Please check email to reset your password!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
                break;
        }
    }

    private boolean validate(){
        if(TextUtils.isEmpty(edtEmail.getText().toString()) ||
                !android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString().trim()).matches()){
            Toast.makeText(context, "Please enter valid email id.", Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }
}
