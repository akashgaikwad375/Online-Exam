package com.exam.onlineexam.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.exam.onlineexam.model.Question;
import com.exam.onlineexam.view.QuestionsFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private ArrayList<com.exam.onlineexam.model.Question> questions;

    public QuestionsPagerAdapter(FragmentManager fm, ArrayList questions) {
        super(fm);
        this.questions = questions;
        fragments = getPages();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    private List<Fragment> getPages(){
        ArrayList pages = new ArrayList<Fragment>();

        Bundle bundle = new Bundle();
        bundle.putInt("index", 0);
        bundle.putSerializable("questions", new ArrayList<Question>(questions.subList(0,10)));
        Fragment first = new QuestionsFragment();
        first.setArguments(bundle);

        Bundle bundle1 = new Bundle();
        bundle1.putInt("index", 10);
        bundle1.putSerializable("questions", new ArrayList<Question>(questions.subList(10,20)));
        Fragment second = new QuestionsFragment();
        second.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putInt("index",20);
        bundle2.putSerializable("questions", new ArrayList<Question>(questions.subList(20,30)));
        Fragment third = new QuestionsFragment();
        third.setArguments(bundle2);

        pages.add(first);
        pages.add(second);
        pages.add(third);

        return pages;
    }
}
