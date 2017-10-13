package com.example.buyulian.study;

import android.app.Notification;
import android.app.NotificationManager;
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

    //Service被启动时调用
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            EncourageContent encourageContent=new EncourageContent();
            @Override
            public void run() {
                int m=60*1000;
                while (true){
                    Date date=new Date();
                    int hours=date.getHours();
                    if(hours>=7&&hours<=21){
                        myNotify(encourageContent.getNextContent(),hours);
                    }
                    try {
                        sleep(60*m);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
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

}
