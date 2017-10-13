package com.example.buyulian.study;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends Activity {
    private Button buttonSave;
    private Button buttonExit;
    private Button buttonReset;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
    }

    private void initView() {
        editText = findViewById(R.id.content_edit);
        buttonSave = findViewById(R.id.buttonSave);
        buttonExit = findViewById(R.id.buttonExit2);
        buttonReset = findViewById(R.id.buttonReset);

        editText.setText(EncourageContent.getSaveStr());

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSp();
            }
        });


        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getApplicationContext().getSharedPreferences("study", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();
                EncourageContent.reset();
                editText.setText(EncourageContent.getSaveStr());
            }
        });

    }

    private void refreshSp() {
        String content = editText.getText().toString();
        String endStr = EncourageContent.endStr;
        String[] strs = content.split(endStr);
        EncourageContent.content = strs;
        SharedPreferences sp = getApplicationContext().getSharedPreferences("study", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("str", EncourageContent.getSaveStr());
        editor.apply();
    }
}
