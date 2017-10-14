package com.example.buyulian.study;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import java.util.Date;

import static java.lang.Thread.sleep;

public class RemindService extends Service {
    //必须要实现的方法
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime=60*60*1000;
                int realCount=0;
                long beginTime=new Date().getTime();
                while (true){
                    Date date=new Date();
                    long nowTime=date.getTime();
                    myNotify(EncourageContent.getRandomContent(),GlobalVariable.notifyCount++);
                    realCount++;
                    long shouldCount=(nowTime-beginTime)/(sleepTime)+1;
                    while (realCount<shouldCount){
                        myNotify(EncourageContent.getRandomContent(),GlobalVariable.notifyCount++);
                        realCount++;
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

    void myNotify(String content,int id){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = builder

                .setContentTitle(content)
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
