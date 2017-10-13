package com.example.buyulian.study;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Button button;
    private Button buttonExit;
    private EditText editText;
    private Chronometer chronometer;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView(){
        editText=findViewById(R.id.editText);
        button=findViewById(R.id.button);
        buttonExit=findViewById(R.id.buttonExit);
        chronometer=findViewById(R.id.chronometer);
        intent=new Intent(this,TimeService.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                startService(intent);
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                String text=chronometer.getText().toString();
                int s=Integer.parseInt(text.replace(':','0'));
                if(s%20==0){
                    String content=Constants.content[s/20%Constants.content.length];
                    Toast.makeText(MainActivity.this,content,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void exit(){
        stopService(intent);
        this.finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
