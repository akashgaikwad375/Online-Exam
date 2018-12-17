package com.exam.onlineexam.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.exam.onlineexam.R;
import com.exam.onlineexam.model.Question;
import com.exam.onlineexam.model.QuestionModel;

import java.util.ArrayList;
import java.util.Arrays;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private Context context;
    private int index;
    private String selectedOptions[];
    private ArrayList<QuestionModel> list;
    private int size = 0;

    public QuestionAdapter(Context context, ArrayList list, int index) {
        this.context = context;
        selectedOptions = new String[10];
        Arrays.fill(selectedOptions, "0");
        this.list = list;
        this.index = index;
        size = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_question, null, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.cvRoot.getLayoutParams().width = size;
        QuestionModel q = list.get(position);
        holder.txtQuestionNo.setText((index + position + 1) + ". ");
        holder.txtQuestion.setText(q.getQuestion());
        holder.rbtnOption1.setText(q.getOption1());
        holder.rbtnOption2.setText(q.getOption2());
        holder.rbtnOption3.setText(q.getOption3());
        holder.rbtnOption4.setText(q.getOption4());

        holder.rbtnOption1.setChecked(q.isOp1Sel());
        holder.rbtnOption2.setChecked(q.isOp2Sel());
        holder.rbtnOption3.setChecked(q.isOp3Sel());
        holder.rbtnOption4.setChecked(q.isOp4Sel());
    }

    public int getSolvedQuestionCount() {
        int count = 0;
        for (int i = 0; i < 10; i++) {
            if (!selectedOptions[i].equals("0"))
                count++;
        }
        return count;
    }

    public int getCorrectAnsCount() {
        int count = 0;
        for (int i = 0; i < 10; i++) {
            if (!selectedOptions[i].equals("0") && selectedOptions[i].equals(list.get(i).getAns()))
                count++;
        }
        return count;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ConstraintLayout cvRoot;
        LinearLayout rgOptions;
        TextView txtQuestionNo, txtQuestion;
        RadioButton rbtnOption1, rbtnOption2, rbtnOption3, rbtnOption4;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            cvRoot = itemView.findViewById(R.id.cl_root);
            txtQuestionNo = itemView.findViewById(R.id.txt_question_no);
            txtQuestion = itemView.findViewById(R.id.txt_question);
            rbtnOption1 = itemView.findViewById(R.id.rbtn_option1);
            rbtnOption2 = itemView.findViewById(R.id.rbtn_option2);
            rbtnOption3 = itemView.findViewById(R.id.rbtn_option3);
            rbtnOption4 = itemView.findViewById(R.id.rbtn_option4);
            rgOptions = itemView.findViewById(R.id.rg_options);

            rbtnOption1.setOnClickListener(this);
            rbtnOption2.setOnClickListener(this);
            rbtnOption3.setOnClickListener(this);
            rbtnOption4.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            QuestionModel q= list.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.rbtn_option1:
                    selectedOptions[getAdapterPosition()] = rbtnOption1.getTag().toString();
                    list.get(getAdapterPosition()).setOp1Sel(true);
                    rbtnOption1.setChecked(q.isOp1Sel());
                    rbtnOption2.setChecked(q.isOp2Sel());
                    rbtnOption3.setChecked(q.isOp3Sel());
                    rbtnOption4.setChecked(q.isOp4Sel());
                    break;
                case R.id.rbtn_option2:
                    selectedOptions[getAdapterPosition()] = rbtnOption2.getTag().toString();
                    list.get(getAdapterPosition()).setOp2Sel(true);
                    rbtnOption1.setChecked(q.isOp1Sel());
                    rbtnOption2.setChecked(q.isOp2Sel());
                    rbtnOption3.setChecked(q.isOp3Sel());
                    rbtnOption4.setChecked(q.isOp4Sel());
                    break;
                case R.id.rbtn_option3:
                    selectedOptions[getAdapterPosition()] = rbtnOption3.getTag().toString();
                    list.get(getAdapterPosition()).setOp3Sel(true);
                    rbtnOption1.setChecked(q.isOp1Sel());
                    rbtnOption2.setChecked(q.isOp2Sel());
                    rbtnOption3.setChecked(q.isOp3Sel());
                    rbtnOption4.setChecked(q.isOp4Sel());
                    break;
                case R.id.rbtn_option4:
                    selectedOptions[getAdapterPosition()] = rbtnOption4.getTag().toString();
                    list.get(getAdapterPosition()).setOp4Sel(true);
                    rbtnOption1.setChecked(q.isOp1Sel());
                    rbtnOption2.setChecked(q.isOp2Sel());
                    rbtnOption3.setChecked(q.isOp3Sel());
                    rbtnOption4.setChecked(q.isOp4Sel());
                    break;
            }
        }
    }
}
