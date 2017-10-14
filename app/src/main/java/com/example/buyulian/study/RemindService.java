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
    ScreenListener listener;
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
                GlobalVariable.isUnlockOn =1;
            }

            @Override
            public void onScreenOff() {

            }

            @Override
            public void onUserPresent() {
                GlobalVariable.isUnlockOn =0;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime=20*60*1000;
                while (true){
                    if(GlobalVariable.isUnlockOn==1){
                        myNotify(EncourageContent.getRandomContent(),GlobalVariable.notifyCount++);
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
