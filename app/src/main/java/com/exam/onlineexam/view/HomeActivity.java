package com.exam.onlineexam.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.exam.onlineexam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.exam.onlineexam.Constants.LOGGED_IN;
import static com.exam.onlineexam.Constants.ONLINE_EXAM;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtLogout, txtUserEmail, txtUserName, txtCheckResults, txtTestResults, txtAddQuestions, lblTitle;
    private SharedPreferences pref;
    private FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private ImageButton btnBack, btnNavBack;
    private Fragment testListFragment;
    private ImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        pref = getSharedPreferences(ONLINE_EXAM, Context.MODE_PRIVATE);

        lblTitle = findViewById(R.id.lbl_title);
        txtUserName = findViewById(R.id.txt_user_name);
        txtUserEmail = findViewById(R.id.txt_user_email);
        txtLogout = findViewById(R.id.txt_logout);
        txtAddQuestions = findViewById(R.id.txt_add_question);
        txtTestResults = findViewById(R.id.txt_test_result);
        txtCheckResults = findViewById(R.id.txt_check_result);
        btnBack = findViewById(R.id.btn_back);
        btnNavBack = findViewById(R.id.btn_nav_back);
        drawerLayout = findViewById(R.id.drawer);
        imgProfile = findViewById(R.id.img_profile);

        testListFragment = new TestListFragment();
        loadFragment(testListFragment, "Home");

        if (auth.getCurrentUser() != null) {
            txtUserEmail.setText(auth.getCurrentUser().getEmail());
            txtUserName.setText(auth.getCurrentUser().getDisplayName());

            File file = new File(getApplicationContext().getFilesDir() + "/TEMP/" + auth.getCurrentUser().getUid() + "/PROFILE_IMAGE.jpg");
            if(file.exists()) {
                Glide.with(this)
                        .load(file)
                        .apply(new RequestOptions().optionalCircleCrop())
                        .into(imgProfile);
            } else {
                Glide.with(this)
                        .load(R.drawable.ic_person_black_24dp)
                        .apply(new RequestOptions().optionalCircleCrop())
                        .into(imgProfile);
            }

            FirebaseDatabase.getInstance().getReference("Admin")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> admins = new ArrayList<String>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                admins.add(dataSnapshot1.getValue(String.class));
                            }
                            if (admins.contains(auth.getCurrentUser().getEmail())) {
                                txtAddQuestions.setVisibility(View.VISIBLE);
                                txtCheckResults.setVisibility(View.VISIBLE);
                            } else {
                                txtAddQuestions.setVisibility(View.GONE);
                                txtCheckResults.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            txtAddQuestions.setVisibility(View.GONE);
                            txtCheckResults.setVisibility(View.GONE);
                        }
                    });
        } else {
            txtAddQuestions.setVisibility(View.GONE);
            txtCheckResults.setVisibility(View.GONE);
            Glide.with(this)
                    .load(R.drawable.ic_person_black_24dp)
                    .apply(new RequestOptions().optionalCircleCrop())
                    .into(imgProfile);
        }
        txtLogout.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnNavBack.setOnClickListener(this);
        txtAddQuestions.setOnClickListener(this);
        txtTestResults.setOnClickListener(this);
        txtCheckResults.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
    }

    private void loadFragment(Fragment fragment, String title) {
        lblTitle.setText(title);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fl_contain, fragment).addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().
                    getBackStackEntryCount() - 1).getName().equals(TestListFragment.class.getSimpleName()))
                finish();
            else {
                getSupportFragmentManager().popBackStack(TestListFragment.class.getSimpleName(), 0);
                lblTitle.setText("Home");
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_nav_back:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.txt_logout:
                auth.signOut();
                pref.edit().putBoolean(LOGGED_IN, false).commit();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.no_anim);
                break;
            case R.id.txt_add_question:
                drawerLayout.closeDrawer(GravityCompat.START);
                Fragment fm = getSupportFragmentManager().findFragmentById(R.id.fl_contain);
                if (!(fm instanceof AddQuestionFragment))
                    loadFragment(new AddQuestionFragment(), "Add Question");
                break;
            case R.id.txt_check_result:
                drawerLayout.closeDrawer(GravityCompat.START);
                Fragment fm1 = getSupportFragmentManager().findFragmentById(R.id.fl_contain);
                if (!(fm1 instanceof CheckUserTestResultFragment))
                    loadFragment(new CheckUserTestResultFragment(), "Check User Results");
                break;
            case R.id.txt_test_result:
                drawerLayout.closeDrawer(GravityCompat.START);
                Fragment fm2 = getSupportFragmentManager().findFragmentById(R.id.fl_contain);
                if (!(fm2 instanceof TestResultFragment))
                    loadFragment(new TestResultFragment(), "Test Results");
                break;
            case R.id.img_profile:
                selectImage();
                break;
            default:
                break;
        }
    }

    private void selectImage() {
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1000);
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{(Manifest.permission.READ_EXTERNAL_STORAGE)},
                    100
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            try {
                final Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                Glide.with(HomeActivity.this).load(b)
                        .apply(new RequestOptions().optionalCircleCrop())
                        .into(imgProfile);
                if (auth.getCurrentUser() != null) {
                    File dir = new File(getApplicationContext().getFilesDir(), "/TEMP/" + auth.getCurrentUser().getUid());
                    if (!dir.exists())
                        dir.mkdirs();
                    File file = new File(getApplicationContext().getFilesDir() + "/TEMP/" + auth.getCurrentUser().getUid() + "/PROFILE_IMAGE.jpg");
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        b.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        }
    }
}
