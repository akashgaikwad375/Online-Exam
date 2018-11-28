package com.exam.onlineexam;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public class ProgressPopUp extends Dialog {

    Context context;

    public ProgressPopUp(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress_loader, null);
        setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void showLoading(){
        show();
    }

    public void dismissLoading(){
        dismiss();
    }
}
