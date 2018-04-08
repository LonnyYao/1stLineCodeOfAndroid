package com.example.ysl.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class PercentFrameLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pfl);

        Toast.makeText(this,
                "Note 'android.support.percent.PercentFrameLayout' is deprecated",
                Toast.LENGTH_LONG).show();
    }
}
