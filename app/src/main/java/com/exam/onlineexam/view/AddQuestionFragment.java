package com.exam.onlineexam.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.exam.onlineexam.ProgressPopUp;
import com.exam.onlineexam.R;
import com.exam.onlineexam.Utils;
import com.exam.onlineexam.model.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddQuestionFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ProgressPopUp popUp;
    private Button btnRetry, btnSubmit;
    private Spinner spin, spinnerAns;
    private EditText edtNewTestName, edtQuestion, edtOption1, edtOption2, edtOption3, edtOption4;
    private ScrollView scrollView;
    private String[] ansArray = new String[]{"1", "2", "3", "4"};
    private String selectTestName= "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_questions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spin = view.findViewById(R.id.spinner_test_names);
        spinnerAns = view.findViewById(R.id.spinner_ans);
        btnRetry = view.findViewById(R.id.btn_retry);
        edtNewTestName = view.findViewById(R.id.edt_new_test);
        scrollView = view.findViewById(R.id.sv_add_question);
        edtQuestion = view.findViewById(R.id.edt_question);
        edtOption1 = view.findViewById(R.id.edt_option1);
        edtOption2 = view.findViewById(R.id.edt_option2);
        edtOption3 = view.findViewById(R.id.edt_option3);
        edtOption4 = view.findViewById(R.id.edt_option4);
        btnSubmit = view.findViewById(R.id.btn_submit);

        setTests();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTests();
            }
        });
        spin.setOnItemSelectedListener(this);
        spinnerAns.setOnItemSelectedListener(this);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate())
                    addQuestion();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_test_names:
                selectTestName = adapterView.getItemAtPosition(i).toString();
                if (selectTestName.equals("New Test")) {
                    edtNewTestName.setVisibility(View.VISIBLE);
                } else edtNewTestName.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setTests() {
        ArrayAdapter ans = new ArrayAdapter(getActivity(), R.layout.test_spinner_item, ansArray);
        ans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAns.setAdapter(ans);
        if (getActivity() != null && Utils.isConnectedToInternet(getActivity())) {
            btnRetry.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            popUp = new ProgressPopUp(getActivity());
            popUp.showLoading();
            FirebaseDatabase.getInstance().getReference("TestNames")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> tests = new ArrayList();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                tests.add(dataSnapshot1.getValue(String.class));
                            }
                            tests.add("New Test");
                            popUp.dismissLoading();
                            ArrayAdapter aa = new ArrayAdapter(getActivity(), R.layout.test_spinner_item, tests.toArray());
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spin.setAdapter(aa);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            popUp.dismissLoading();
                        }
                    });
        } else {
            scrollView.setVisibility(View.GONE);
            btnRetry.setVisibility(View.VISIBLE);
        }
    }

    private boolean validate() {
        if (edtNewTestName.getVisibility() == View.VISIBLE && TextUtils.isEmpty(edtNewTestName.getText().toString())) {
            edtNewTestName.setError("Test name is empty");
            edtNewTestName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(edtQuestion.getText().toString())) {
            edtQuestion.setError("Question is empty");
            edtQuestion.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtOption1.getText().toString())) {
            edtOption1.setError("Option 1 is empty");
            edtOption1.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtOption2.getText().toString())) {
            edtOption2.setError("Option 2 is empty");
            edtOption2.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtOption3.getText().toString())) {
            edtOption3.setError("Option 3 is empty");
            edtOption3.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(edtOption4.getText().toString())) {
            edtOption4.setError("Option 4 is empty");
            edtOption4.requestFocus();
            return false;
        } else return true;
    }

    void addQuestion() {
        if (getActivity() != null && Utils.isConnectedToInternet(getActivity())) {
            Question question = Question.createQuestion(edtQuestion.getText().toString(),
                    edtOption1.getText().toString(), edtOption2.getText().toString(),
                    edtOption3.getText().toString(), edtOption4.getText().toString(),
                    String.valueOf(spinnerAns.getSelectedItemPosition()+1));

            if(TextUtils.isEmpty(selectTestName))
                selectTestName = spin.getSelectedItem().toString();

            String testName = selectTestName;
            if (selectTestName.equals("New Test")) {
                testName = edtNewTestName.getText().toString().trim();
                DatabaseReference testReference = FirebaseDatabase.getInstance().getReference("TestNames");
                String key = testReference.push().getKey();
                testReference.child(key).setValue(testName);
            }

            DatabaseReference questionRef = FirebaseDatabase.getInstance().getReference("Questions/"+testName);
            String key = questionRef.push().getKey();
            questionRef.child(key).setValue(question);
            edtQuestion.setText("");
            edtOption1.setText("");
            edtOption2.setText("");
            edtOption3.setText("");
            edtOption4.setText("");
            spinnerAns.setSelection(0);
            Toast.makeText(getActivity(), "Question added", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        edtNewTestName.clearFocus();
    }
}
