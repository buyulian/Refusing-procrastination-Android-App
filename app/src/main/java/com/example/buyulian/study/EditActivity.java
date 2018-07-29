package com.example.buyulian.study;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EditActivity extends Activity {

    @BindView(R.id.buttonSave)
    Button buttonSave;

    @BindView(R.id.buttonExit2)
    Button buttonExit;

    @BindView(R.id.buttonReset)
    Button buttonReset;

    @BindView(R.id.content_edit)
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

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
        String endStr = EncourageContent.END_STR;
        String[] strs = content.split(endStr);
        EncourageContent.content = strs;
        SharedPreferences sp = getApplicationContext().getSharedPreferences("study", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("str", EncourageContent.getSaveStr());
        editor.apply();
    }
}
