package com.example.ysl.activitylifecycletest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DialogActivity extends AppCompatActivity {
    private static final String TAG = "DialogActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "--- OnStart ---");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "--- OnResume ---");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "--- OnPause ---");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "--- OnStop ---");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "--- onDestroy ---");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "--- OnRestart ---");
    }
}
