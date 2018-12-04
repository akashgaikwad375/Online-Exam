package com.exam.onlineexam.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.Toast;

import com.exam.onlineexam.ProgressPopUp;
import com.exam.onlineexam.R;
import com.exam.onlineexam.Utils;
import com.exam.onlineexam.adapter.TestAdapter;
import com.exam.onlineexam.model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.exam.onlineexam.Constants.TEST_QUESTIONS;
import static com.exam.onlineexam.Constants.TEST_SELECTED;

public class TestListFragment extends Fragment {

    RecyclerView rvTests;
    private ProgressPopUp popUp;
    private TestAdapter adapter;
    private Button btnRetry;
    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnRetry = view.findViewById(R.id.btn_retry);
        rvTests = view.findViewById(R.id.rv_tests);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTests();
            }
        });
        setTests();
    }

    private void setTests() {
        if (getActivity()!= null && Utils.isConnectedToInternet(getActivity())) {
            btnRetry.setVisibility(View.GONE);
            popUp = new ProgressPopUp(getActivity());
            popUp.showLoading();
            FirebaseDatabase.getInstance().getReference("TestNames")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList tests = new ArrayList();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                tests.add(dataSnapshot1.getValue());
                            }
                            popUp.dismissLoading();
                            adapter = new TestAdapter(getActivity(), tests,
                                    new TestAdapter.OnTestStartClickListener() {
                                        @Override
                                        public void onStartClick(View view, int position, final String testName) {
                                            if (dialog == null) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                                        .setTitle("Test Instructions")
                                                        .setMessage(R.string.test_rules)
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                                if (dialog != null) {
                                                                    dialog.dismiss();
                                                                    dialog = null;
                                                                }
                                                                getQuestions(testName);
                                                            }
                                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                                if (dialog != null) {
                                                                    dialog.dismiss();
                                                                    dialog = null;
                                                                }
                                                            }
                                                        }).setCancelable(false);
                                                dialog = builder.create();
                                                dialog.show();
                                            }
                                        }
                                    });
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_from_bottom);
                            rvTests.setAdapter(adapter);
                            rvTests.addItemDecoration(new SpacesItemDecoration(48));
                            rvTests.setLayoutAnimation(animation);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            popUp.dismissLoading();
                        }
                    });
        } else btnRetry.setVisibility(View.VISIBLE);
    }

    private void getQuestions(final String testName) {
        if (Utils.isConnectedToInternet(getActivity())) {
            popUp = new ProgressPopUp(getActivity());
            popUp.showLoading();
            FirebaseDatabase.getInstance().getReference("Questions/" + testName)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            popUp.dismissLoading();
                            ArrayList<Question> tests = new ArrayList<Question>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                tests.add(dataSnapshot1.getValue(Question.class));
                            }
                            Collections.shuffle(tests);
                            ArrayList<Question> tests1 = new ArrayList<Question>();
                            if (tests.size() >= 30) {
                                for (int i = 0; i < 30; i++) {
                                    tests1.add(tests.get(i));
                                }
                                startActivity(new Intent(getActivity(), TestActivity.class)
                                        .putExtra(TEST_SELECTED, testName)
                                        .putExtra(TEST_QUESTIONS, tests1));
                                ((HomeActivity)getActivity()).overridePendingTransition(R.anim.slide_from_right, R.anim.no_anim);
                            } else {
                                Toast.makeText(getActivity(), "Data not available", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            popUp.dismissLoading();
                        }
                    });
        }
    }
}
