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
import java.util.Timer;

import static java.lang.Thread.sleep;

public class RemindService extends Service {
    ScreenListener listener;
    Thread singleTime=new Thread(new Runnable() {
        @Override
        public void run() {
            int gapTime=10;
            int sleepTime=gapTime*60*1000;
            int ct=1;
            while (GlobalVariable.isUnlockOn==1){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    return;
                }
                String title="你已连续玩手机 "+ct*gapTime+" 分钟,快去学习";
                myNotify(title,GlobalVariable.notifyCount++);
                ct++;
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
            @Override
            public void onScreenOn() {
            }

            @Override
            public void onScreenOff() {
                GlobalVariable.isUnlockOn =0;
                singleTime.interrupt();
            }

            @Override
            public void onUserPresent() {
                GlobalVariable.isUnlockOn =1;
                singleTime.start();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                int gapTime=20;
                int sleepTime=gapTime*60*1000;
                int realCount=0;
                int shCount=0;
                while (true){
                    int msg=shCount*gapTime;
                    GlobalVariable.dayUsedTime=msg;
                    shCount++;
                    if(GlobalVariable.isUnlockOn==1){
                        while (realCount<shCount){
                            myNotify("今天都玩手机 "+realCount*gapTime+" 分钟啦，玩物丧志",GlobalVariable.notifyCount++);
                            realCount++;
                        }
                    }
                    try {
                        sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
