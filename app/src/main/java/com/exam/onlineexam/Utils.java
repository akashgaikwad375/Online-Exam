package com.exam.onlineexam;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

    public static boolean isConnectedToInternet(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(activity,"No Internet Connection", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public static String getCurrentDate(){
        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy hh:mm:ss");
        return df.format(Calendar.getInstance().getTimeInMillis());
    }
}
