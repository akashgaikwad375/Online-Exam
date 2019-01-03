package com.exam.onlineexam.view;

import android.content.DialogInterface;
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

import com.exam.onlineexam.ProgressPopUp;
import com.exam.onlineexam.R;
import com.exam.onlineexam.Utils;
import com.exam.onlineexam.adapter.TestAdapter;
import com.exam.onlineexam.adapter.TestResultAdapter;
import com.exam.onlineexam.model.Result;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class TestResultFragment extends Fragment {

    private RecyclerView rvTestResults;
    private TestResultAdapter adapter;
    private ProgressPopUp popUp;
    private FirebaseAuth auth;
    private Button btnRetry;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        rvTestResults = view.findViewById(R.id.rv_test_results);
        btnRetry = view.findViewById(R.id.btn_retry);
        int size = (int) (getActivity().getResources().getDisplayMetrics().widthPixels*0.9);
        adapter = new TestResultAdapter(getActivity(), null, size);
        rvTestResults.addItemDecoration(new SpacesItemDecoration(48));
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_from_bottom);
        rvTestResults.setAdapter(adapter);
        rvTestResults.setLayoutAnimation(animation);
        setResult();
    }

    private void setResult(){
        if (getActivity()!= null && Utils.isConnectedToInternet(getActivity()) && auth.getCurrentUser() != null) {
            btnRetry.setVisibility(View.GONE);
            popUp = new ProgressPopUp(getActivity());
            popUp.showLoading();
            String userId = auth.getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference("Results/"+userId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<Result> results = new ArrayList();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                results.add(dataSnapshot1.getValue(Result.class));
                            }
                            popUp.dismissLoading();
                            if(!results.isEmpty() && adapter != null){
                                Collections.reverse(results);
                                adapter.setList(results);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            popUp.dismissLoading();
                        }
                    });
        } else btnRetry.setVisibility(View.VISIBLE);
    }
}
