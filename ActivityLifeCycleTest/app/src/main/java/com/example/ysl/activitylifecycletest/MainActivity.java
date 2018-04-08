package com.example.ysl.activitylifecycletest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt_normal = findViewById(R.id.bt_normal);
        bt_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Here will start NormalActivity.");
                startActivity(new Intent(MainActivity.this, NormalActivity.class));
            }
        });

        Button bt_dialog = findViewById(R.id.bt_dialog);
        bt_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Here will start NormalActivity.");
                startActivity(new Intent(MainActivity.this, DialogActivity.class));
            }
        });
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
