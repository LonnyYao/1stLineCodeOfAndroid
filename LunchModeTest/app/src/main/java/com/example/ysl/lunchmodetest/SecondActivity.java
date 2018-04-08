package com.example.ysl.lunchmodetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "LonnyDebug" + "Second";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,this.toString()
                + ", Task id: " + getTaskId());
        setContentView(R.layout.activity_second);

        Button btStartPre = findViewById(R.id.bt_start_previous);
        btStartPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, MainActivity.class));
                startActivity(new Intent(SecondActivity.this, MainActivity.class));
                Log.i(TAG, "--- After start activity. ---");
            }
        });

        Button btStartThird = findViewById(R.id.bt_start_third);
        btStartThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, ThirdActivity.class));
                Log.i(TAG, "--- After start activity. ---");
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "--- onRestart. ---");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "--- onDestroy. ---");
    }
}
