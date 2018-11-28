package com.exam.onlineexam.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exam.onlineexam.R;
import com.exam.onlineexam.adapter.QuestionAdapter;

import java.util.ArrayList;

public class QuestionsFragment extends Fragment {

    private RecyclerView rvQuestions;
    private QuestionAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rvQuestions = view.findViewById(R.id.rv_questions);
        ArrayList list = (ArrayList) getArguments().getSerializable("questions");
        int index = getArguments().getInt("index");
        mAdapter = new QuestionAdapter(getActivity(), list, index);
        rvQuestions.addItemDecoration(new SpacesItemDecoration(48));
        rvQuestions.setAdapter(mAdapter);
    }

    public int getCorrectAnsCount() {
        return mAdapter.getCorrectAnsCount();
    }

    public int getSolvedQuestionCount() {
        return mAdapter.getSolvedQuestionCount();
    }
}
