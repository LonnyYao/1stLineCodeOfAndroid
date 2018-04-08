package com.example.ysl.filepersistence;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String STORED_FILE_NAME = "editContent";

    private EditText etStore;
    private Button btStore;

    private String storeContent = null;
    private boolean isChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etStore = findViewById(R.id.et_store);
        btStore = findViewById(R.id.bt_store);

        String storedText = restoreText(STORED_FILE_NAME);
        if (!TextUtils.isEmpty(storedText)) {
            etStore.setText(storedText);
            etStore.setSelection(storedText.length());
            Toast.makeText(this, "Restore the text content success.",
                    Toast.LENGTH_SHORT).show();
        }

        btStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isChanged) {
                    String str = etStore.getText().toString();
                    if (!TextUtils.isEmpty(str)) {
                        if (storeContent == null || !storeContent.equals(str)) {

                            saveText(str);

                            storeContent = str;
                            isChanged = true;

                            Toast.makeText(MainActivity.this, "The text is stored succeed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "onClick: text content not change!");
                            isChanged = false;
                        }
                    } else {
                        Log.w(TAG, "onClick: text content is empty!");
                        isChanged = false;
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!isChanged) {
            String content = etStore.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                Log.w(TAG, "onDestroy: save the text content automatically.");
                saveText(content);
            }
        }
    }

    /**
     * Store the string to the file
     *
     * @param text the text content that be stored to file
     */
    private void saveText(String text) {

        FileOutputStream output = null;
        BufferedWriter writer = null;

        try {
            output = openFileOutput(STORED_FILE_NAME, MODE_PRIVATE);
            //output = openFileOutput("editContent", MODE_APPEND);
            writer = new BufferedWriter(new OutputStreamWriter(output));
            writer.write(text);
            Log.i(TAG, "saveText: write the text content: " + text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                    Log.i(TAG, "saveText: write closed.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the restored text content from the specified file.
     *
     * @param fileName the file name of the restore file.
     * @return the stored text content if success, null if failed.
     */
    private String restoreText(String fileName) {

        FileInputStream input = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();

        try {
            input = openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(input));
            String line;
            while((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (content != null && !content.toString().isEmpty())
            return content.toString();
        else
            return null;
    }
}
