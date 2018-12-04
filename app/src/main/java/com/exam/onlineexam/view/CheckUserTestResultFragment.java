package com.exam.onlineexam.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.exam.onlineexam.ProgressPopUp;
import com.exam.onlineexam.R;
import com.exam.onlineexam.Utils;
import com.exam.onlineexam.adapter.TestResultAdapter;
import com.exam.onlineexam.model.Result;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckUserTestResultFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerTestName, spinnerUser;
    private Button btnSubmit, btnRetry;
    private RecyclerView rvResult;
    private ConstraintLayout clRoot;
    private ProgressPopUp popUp;
    private EditText edtUserEmail;
    private FirebaseAuth auth;
    private TestResultAdapter adapter;
    private String blockCharacterSet = ".#$[]";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check_user_test_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spinnerUser = view.findViewById(R.id.spinner_user_email);
        btnRetry = view.findViewById(R.id.btn_retry);
        btnSubmit = view.findViewById(R.id.btn_submit);
        clRoot = view.findViewById(R.id.cl_root);
        rvResult = view.findViewById(R.id.rv_test_results);
        edtUserEmail = view.findViewById(R.id.edt_user_email);

        edtUserEmail.setFilters(new InputFilter[] { filter });

        /*btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUser();
            }
        });*/
        spinnerUser.setOnItemSelectedListener(this);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate())
                    getTestResults();
            }
        });
    }

    private boolean validate() {
        String userId = edtUserEmail.getText().toString().trim();
        if (TextUtils.isEmpty(userId)) {
            edtUserEmail.setError("User Id is empty");
            edtUserEmail.requestFocus();
            return false;
        }else return true;
    }

    private void getTestResults() {
        if (getActivity() != null && Utils.isConnectedToInternet(getActivity())) {
            popUp = new ProgressPopUp(getActivity());
            popUp.showLoading();
            FirebaseDatabase.getInstance().getReference("Results/" + edtUserEmail.getText().toString().trim())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<Result> results = new ArrayList();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                results.add(dataSnapshot1.getValue(Result.class));
                            }
                            popUp.dismissLoading();
                            if (!results.isEmpty()) {
                                adapter = new TestResultAdapter(getActivity(), results);
                                rvResult.addItemDecoration(new SpacesItemDecoration(48));
                                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_animation_from_bottom);
                                rvResult.setAdapter(adapter);
                                rvResult.setLayoutAnimation(animation);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            popUp.dismissLoading();
                        }
                    });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
}
