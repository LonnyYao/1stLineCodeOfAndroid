package com.example.ysl.lunchmodetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class ThirdActivity extends AppCompatActivity {
    private static final String TAG = "LonnyDebug" + "Third";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,this.toString()
                + ", Task id: " + getTaskId());
        setContentView(R.layout.activity_third);
    }
}
