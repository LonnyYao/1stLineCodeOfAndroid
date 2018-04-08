package com.example.ysl.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class PercentRelativeLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prl);

        Toast.makeText(this,
                "Note 'android.support.percent.PercentRelativeLayout' is deprecated",
                Toast.LENGTH_LONG).show();
    }
}
