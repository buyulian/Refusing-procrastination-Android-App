package com.example.buyulian.study;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;


import java.util.Date;

import static java.lang.Thread.sleep;

public class RemindService extends Service {
    private ScreenListener listener;
    private Thread singleTime=new Thread(new Runnable() {
        @Override
        public void run() {
            int gapTime=10;
            int sleepTime=gapTime*60*1000;
            int ct=1;
            while (true){
                try {
                    sleep(sleepTime);
                    String title="你已连续玩手机 "+ct*gapTime+" 分钟,快去学习吧";
                    myNotify(title,GlobalVariable.notifyCount++);
                    ct++;
                } catch (InterruptedException e) {
                    ct=1;
                }
            }
        }
    });
    //必须要实现的方法
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        listener =new ScreenListener(this);
        listener.register(new ScreenListener.ScreenStateListener() {

            private long lockTime=System.currentTimeMillis();
            private long unlockTime=lockTime;
            private long lastLockTime=lockTime;
            private long lastUnlockTime=lockTime;
            private long minTimeGap=5*60*1000;
            private long minOnTimeGap=60*1000;

            @Override
            public void onScreenOn() {
            }

            @Override
            public void onScreenOff() {
                GlobalVariable.isUnlockOn =0;
                long local=System.currentTimeMillis();
                if(local-unlockTime>minOnTimeGap){
                    lastLockTime=lockTime;
                    lockTime=local;
                }else{
                    unlockTime=lastUnlockTime;
                }
            }

            @Override
            public void onUserPresent() {
                GlobalVariable.isUnlockOn =1;
                long local=System.currentTimeMillis();
                if(local-lockTime>minTimeGap){
                    singleTime.interrupt();
                    lastUnlockTime=unlockTime;
                    unlockTime=local;
                }
            }
        });

        Thread thread=new Thread(){
            @Override
            public void run() {
                int gapTime=20;
                int sleepTime=gapTime*60*1000;
                int ct=0;
                Date lastTime=new Date();
                int lastHours=lastTime.getHours();
                while (true){
                    int nowHours=new Date().getHours();
                    if(nowHours<lastHours){
                        lastHours=nowHours;
                        ct=0;
                    }
                    int msg=ct*gapTime;
                    GlobalVariable.dayUsedTime=msg;
                    myNotify("今天已玩手机 "+ct*gapTime+" 分钟，当心玩物丧志",GlobalVariable.notifyCount++);
                    ct++;
                    try {
                        sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        singleTime.start();
    }

    //Service被启动时调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        if (listener != null) {
            listener.unregister();
        }
        super.onDestroy();
    }



    void myNotify(String title,int id){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = builder

                .setContentTitle(title)
                .setContentText(EncourageContent.getRandomContent())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_now)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_now))
                .build();
        manager.notify(id,notification);

    }

}
