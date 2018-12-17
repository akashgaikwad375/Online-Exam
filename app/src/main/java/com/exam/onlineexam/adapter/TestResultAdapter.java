package com.exam.onlineexam.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.exam.onlineexam.R;
import com.exam.onlineexam.model.Result;

import java.util.ArrayList;

public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.TestResultViewHolder>{

    private Context context;
    private int size = 0;
    private ArrayList<Result> results;

    public TestResultAdapter(Context context, ArrayList<Result> list, int size){
        this.context = context;
        results = list;
        this.size = size;
    }

    @NonNull
    @Override
    public TestResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_test_result, null, false);
        return new TestResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestResultViewHolder holder, int position) {
        holder.clRoot.getLayoutParams().width = size;
        holder.txtAttempedCount.setText(String.format("%02d", results.get(position).getQuestionAttemted()));
        holder.txtTestName.setText(results.get(position).getTestName());
        holder.txtMarks.setText(String.format("%02d", results.get(position).getTestMarks())+"/30");
        holder.txtDate.setText("Date: "+results.get(position).getTestDate());
    }

    public void setList(ArrayList<Result> results){
        this.results = results;
    }

    @Override
    public int getItemCount() {
        if(results == null)
            return 0;
        else return results.size();
    }

    class TestResultViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout clRoot;
        TextView txtTestName, txtAttempedCount, txtMarks, txtDate;

        public TestResultViewHolder(View itemView) {
            super(itemView);
            clRoot = itemView.findViewById(R.id.cl_root);
            txtTestName = itemView.findViewById(R.id.txt_name);
            txtAttempedCount = itemView.findViewById(R.id.txt_attempted_count);
            txtMarks = itemView.findViewById(R.id.txt_ans_count);
            txtDate = itemView.findViewById(R.id.txt_date);
        }
    }
}
