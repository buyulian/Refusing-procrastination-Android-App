package com.example.buyulian.study;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import java.util.Date;

public class MainActivity extends Activity {
    private Button button;
    private Button buttonExit;
    private Button buttonEdit;
    private Button buttonNext;
    private Button buttonRandomNext;
    private Spinner spinner;
    private TextView chronometer;
    private TextView dayUsedTime;
    private TextView oneUsedTime;
    private Intent intent;
    private Intent intentEdit;
    private boolean isFinished=false;
    private int limitTime=180;
    boolean change=false;
    private Thread thread;

    final Handler handler=new Handler(){
        private boolean t=true;
        @Override
        public void handleMessage(Message msg) {
            int seconds=msg.what;
            if(change){
                if(t){
                    chronometer.setTextColor(0xffff0000);
                    t=false;
                }
                chronometer.setText(intToTime(seconds-limitTime));
            }else{
                chronometer.setText(intToTime(seconds));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initString();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        int nowDay=new Date().getDay();
        if(nowDay>GlobalVariable.startUpDay){
            GlobalVariable.totalTime=0;
            GlobalVariable.startUpDay=nowDay;
        }
        long local=System.currentTimeMillis()-GlobalVariable.unlockTime;
        long andTime=GlobalVariable.totalTime+local;
        dayUsedTime.setText(intToTime((int)(andTime/Constants.SECOND)));

        andTime=GlobalVariable.oneTime+local;
        oneUsedTime.setText(intToTime((int)(andTime/Constants.SECOND)));
    }

    private void initView(){
        button=findViewById(R.id.button);
        buttonExit=findViewById(R.id.buttonExit);
        buttonEdit=findViewById(R.id.buttonEdit);
        buttonNext=findViewById(R.id.buttonNext);
        buttonRandomNext=findViewById(R.id.buttonRandomNext);
        spinner=findViewById(R.id.spinner1);
        chronometer=findViewById(R.id.chronometer);
        dayUsedTime=findViewById(R.id.dayUsedTime);
        oneUsedTime=findViewById(R.id.oneUsedTime);

        intent=new Intent(this,RemindService.class);
        intentEdit=new Intent(this,EditActivity.class);

        startService(intent);

        chronometer.setText(intToTime(0));

        if(GlobalVariable.unlockTime<100){
            GlobalVariable.unlockTime=System.currentTimeMillis();
        }
        int nowDay=new Date().getDay();
        if(nowDay>GlobalVariable.startUpDay){
            GlobalVariable.totalTime=0;
            GlobalVariable.startUpDay=nowDay;
        }
        long local=System.currentTimeMillis()-GlobalVariable.unlockTime;
        long andTime=GlobalVariable.totalTime+local;
        dayUsedTime.setText(intToTime((int)(andTime/Constants.SECOND)));

        andTime=GlobalVariable.oneTime+local;
        oneUsedTime.setText(intToTime((int)(andTime/Constants.SECOND)));


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
//                chronometer.setText(intToTime(seconds));
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentEdit);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            EncourageContent encourageContent=new EncourageContent();
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,encourageContent.getNextContent(),0).show();
            }
        });

        buttonRandomNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,EncourageContent.getRandomContent(),0).show();
            }
        });

    }

    private String intToTime(int seconds){
        int hours=seconds/3600;
        int minutes=seconds/60%60;
        int second=seconds%60;
        String rs=getTwoInt(hours)+":"+getTwoInt(minutes)+":"+getTwoInt(second);
        return rs;
    }

    private String getTwoInt(int a){
        if(a<10){
            return "0"+a;
        }
        return String.valueOf(a);
    }

    private void encourageBegin(){
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                beginTime();
            }
        });
        thread.start();
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


    void myNotify(String content,int id,int superTime,long millisTime){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = builder

                .setContentTitle("你已拖延"+Tools.getTimeString(superTime))
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setWhen(millisTime)
                .setSmallIcon(R.mipmap.ic_launcher_now)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_now))
                .build();
        manager.notify(id,notification);

    }

    private void initString(){
        SharedPreferences sp= getApplicationContext().getSharedPreferences("study", Context.MODE_PRIVATE);
        String str=sp.getString("str","");
        if(!"".equals(str)){
            EncourageContent.setSaveStr(str);
        }
    }

    private void beginTime(){
        EncourageContent encourageContent=new EncourageContent();
        long beginMill=System.currentTimeMillis();
        int maxD=EncourageContent.GAP_TIME[EncourageContent.GAP_TIME.length-1];
        int realCount=0;
        while (!isFinished){
            long nowWill=System.currentTimeMillis();
            int dValue=(int)(nowWill-beginMill)/1000;
            handler.sendEmptyMessage(dValue);
            if(dValue-limitTime>maxD){
                return;
            }
            if(!change){
                if(dValue>limitTime){
                    change=true;
                }
            }else{
                int superTime=dValue-limitTime;
                if(encourageContent.isRemind(superTime)){
                    String content=encourageContent.getNextContent();
                    myNotify(content,GlobalVariable.notifyCount++,superTime,nowWill);
                    realCount++;
                }
                int shouldCount=EncourageContent.getTimes(superTime);
                while (realCount<shouldCount){
                    String content=encourageContent.getNextContent();
                    int shTime=EncourageContent.GAP_TIME[realCount];
                    long realTime=beginMill+shTime*1000;
                    myNotify(content,GlobalVariable.notifyCount++,shTime,realTime);
                    realCount++;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
