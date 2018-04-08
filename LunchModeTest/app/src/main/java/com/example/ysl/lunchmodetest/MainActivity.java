package com.example.ysl.lunchmodetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LonnyDebug" + "Main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, this.toString()
                + ", Task id: " + getTaskId());
        setContentView(R.layout.activity_main);

        Button btStart = findViewById(R.id.bt_start);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, MainActivity.class));
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
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
