package com.example.buyulian.study;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import java.util.Date;

public class MainActivity extends Activity {
    private Button button;
    private Button buttonExit;
    private Spinner spinner;
    private Chronometer chronometer;
    private boolean isFinished=false;
    private int limitTime=180;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView(){
        button=findViewById(R.id.button);
        buttonExit=findViewById(R.id.buttonExit);
        spinner=findViewById(R.id.spinner1);
        chronometer=findViewById(R.id.chronometer);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {

                //拿到被选择项的值
                String str = (String) spinner.getSelectedItem();
                //把该值传给 limitTime
                limitTime=Integer.parseInt(str)*60;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setEnabled(false);
                encourageBegin();
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            boolean change=false;
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if(!change){
                    String text=chronometer.getText().toString();
                    String[] a=text.split(":") ;
                    int s=0;
                    for(String b:a){
                        int c=Integer.parseInt(b);
                        s*=60;
                        s+=c;
                    }
                    if(s>limitTime){
                        change=true;
                        chronometer.setTextColor(0xAAff0000);
                        chronometer.setBase(SystemClock.elapsedRealtime());
                    }
                }

            }
        });

    }

    private void encourageBegin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                beginTime();
            }
        }).start();
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
        boolean change=false;
        while (!isFinished){
            long nowWill=new Date().getTime();
            int DValue=(int)(nowWill-beginMill)/1000;
            if(!change){
                if(DValue>limitTime){
                    change=true;
                }
            }else{
                if(encourageContent.isRemind(DValue-limitTime)){
                    String content=encourageContent.getNextContent();
                    myNotify(content,DValue/20);
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
