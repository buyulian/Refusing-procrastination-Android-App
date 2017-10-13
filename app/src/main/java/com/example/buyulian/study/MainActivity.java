package com.example.buyulian.study;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
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

import java.util.Date;

public class MainActivity extends Activity {
    private Button button;
    private Button buttonExit;
    private EditText editText;
    private Chronometer chronometer;
    private Intent intent;
    private boolean isFinished=false;
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        beginTime();
                    }
                }).start();
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            EncourageContent encourageContent=new EncourageContent();
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                String text=chronometer.getText().toString();
                int s=Integer.parseInt(text.replace(':','0'));
                if(s%20==0){
                    String content= encourageContent.getNextContent();
                    Toast.makeText(MainActivity.this,content,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void exit(){
        isFinished=true;
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


    void myNotify(String content,int id){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Notification notification = builder

                .setContentTitle(content)

                .setContentText("Now 马上去做")

                .setWhen(System.currentTimeMillis())

                .setSmallIcon(R.mipmap.ic_launcher_now)

                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_now))

                .build();
        manager.notify(id,notification);

    }

    private void beginTime(){
        EncourageContent encourageContent=new EncourageContent();
        long beginMill=new Date().getTime();
        while (!isFinished){
            long nowWill=new Date().getTime();
            int DValue=(int)(nowWill-beginMill)/1000;
            if(DValue%20==0){
                String content=encourageContent.getNextContent();
                myNotify(content,DValue/20);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
