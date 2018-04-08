package com.example.ysl.simpleuitest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private int imgCount = 0;
    private int image[] = new int[]{
            R.drawable.img_iv_1,
            R.drawable.img_iv_2
    };

    private Button button;
    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.bt_this);
        imageView = findViewById(R.id.iv_this);
        progressBar = findViewById(R.id.pb_this);

        button.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_this:
                ChangeImg();
                break;
            case R.id.bt_this:
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    private void  ChangeImg () {
        if(imgCount >= 2 || imgCount < 0)
            imgCount = 0;

        imageView.setImageResource(image[imgCount%image.length]);
        imgCount++;
    }
}
