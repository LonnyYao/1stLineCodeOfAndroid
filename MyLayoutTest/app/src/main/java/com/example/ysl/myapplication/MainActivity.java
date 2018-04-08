package com.example.ysl.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    private Button btHorizontalLL;
    private Button btRL; // Relative Layout
    private Button btFL; // Frame Layout
    private Button btPFL; // Percent Frame Layout
    private Button btPRL; // Percent Relative Layout


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btHorizontalLL = findViewById(R.id.bt_start_ll_hor);
        btRL = findViewById(R.id.bt_start_rl);
        btFL = findViewById(R.id.bt_start_fl);
        btPFL = findViewById(R.id.bt_start_pfl);
        btPRL = findViewById(R.id.bt_start_prl);

        btHorizontalLL.setOnClickListener(this);
        btRL.setOnClickListener(this);
        btFL.setOnClickListener(this);
        btPFL.setOnClickListener(this);
        btPRL.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_start_ll_hor:
                Log.i(TAG, "onClick: ll_hor");
                startActivity(new Intent(MainActivity.this, HorizontalLLActivity.class));
                break;
            case R.id.bt_start_rl:
                Log.i(TAG, "onClick: rl");
                startActivity(new Intent(MainActivity.this, RelativeLayoutActivity.class));
                break;
            case R.id.bt_start_fl:
                Log.i(TAG, "onClick: fl");
                startActivity(new Intent(MainActivity.this, FrameLayoutActivity.class));
                break;
            case R.id.bt_start_pfl:
                Log.i(TAG, "onClick: pfl");
                startActivity(new Intent(MainActivity.this, PercentFrameLayoutActivity.class));
                break;
            case R.id.bt_start_prl:
                Log.i(TAG, "onClick: prl");
                startActivity(new Intent(MainActivity.this, PercentRelativeLayoutActivity.class));
                break;
            default:
                break;
        }

    }
}
