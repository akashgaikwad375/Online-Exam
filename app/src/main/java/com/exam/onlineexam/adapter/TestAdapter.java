package com.exam.onlineexam.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.exam.onlineexam.R;
import com.exam.onlineexam.view.HomeActivity;
import com.exam.onlineexam.view.TestActivity;
import com.google.firebase.database.ChildEventListener;

import java.util.ArrayList;

import static com.exam.onlineexam.Constants.TEST_SELECTED;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder>{
    Context context;
    ArrayList<String> tests;
    private OnTestStartClickListener listener;
    private int testSize = 0;

    public TestAdapter(Context context, ArrayList<String> tests, OnTestStartClickListener listener) {
        this.context =context;
        this.tests = tests;
        this.listener = listener;
        testSize = (int) (context.getResources().getDisplayMetrics().widthPixels*0.5);
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_test, null, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        holder.btnTestName.getLayoutParams().width = testSize;
        holder.btnTestName.setText(tests.get(position));
        /*Animation anim = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_bottom);
        anim.setInterpolator(context, android.R.anim.bounce_interpolator);
        holder.btnTestName.startAnimation(anim);*/
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    class TestViewHolder extends RecyclerView.ViewHolder{

        Button btnTestName, btnStart;

        public TestViewHolder(View itemView) {
            super(itemView);
            btnTestName = itemView.findViewById(R.id.btn_test_name);
            btnStart = itemView.findViewById(R.id.btn_start);

            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onStartClick(view, getAdapterPosition(), tests.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnTestStartClickListener{
        void onStartClick(View view, int position, String testName);
    }
}
