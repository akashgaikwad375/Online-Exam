package com.exam.onlineexam.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.exam.onlineexam.R;
import com.exam.onlineexam.model.Question;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>{

    private Context context;
    private int index;
    private String selectedOptions[];
    private ArrayList<Question> list;
    private int size = 0;

    public QuestionAdapter(Context context, ArrayList list, int index) {
        this.context = context;
        selectedOptions = new String[10];
        Arrays.fill(selectedOptions, "0");
        this.list = list;
        this.index= index;
        size = (int) (context.getResources().getDisplayMetrics().widthPixels*0.9);
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
        Question q= list.get(position);
        holder.txtQuestionNo.setText((index+position+1)+". ");
        holder.txtQuestion.setText(q.getQuestion());
        holder.rbtnOption1.setText(q.getOption1());
        holder.rbtnOption2.setText(q.getOption2());
        holder.rbtnOption3.setText(q.getOption3());
        holder.rbtnOption4.setText(q.getOption4());
    }

    public int getSolvedQuestionCount(){
        int count = 0;
        for(int i =0; i<10; i++){
            if(!selectedOptions[i].equals("0"))
                count++;
        }
        return count;
    }

    public int getCorrectAnsCount(){
        int count = 0;
        for(int i =0; i<10; i++){
            if(!selectedOptions[i].equals("0") && selectedOptions[i].equals(list.get(i).getAns()))
                count++;
        }
        return count;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout cvRoot;
        RadioGroup rgOptions;
        TextView txtQuestionNo, txtQuestion;
        RadioButton rbtnOption1, rbtnOption2 ,rbtnOption3 ,rbtnOption4 ;

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

            rgOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    RadioButton radioButton =  radioGroup.findViewById(i);
                    selectedOptions[getAdapterPosition()] = radioButton.getTag().toString();
                }
            });
        }
    }
}
