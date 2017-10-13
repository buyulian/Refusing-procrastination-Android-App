package com.example.buyulian.study;

import android.app.*;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import java.util.Date;

public class TimeService extends Service {

    long beginMill;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        beginMill=new Date().getTime();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                beginTime();
            }

            private void beginTime(){
                while (true){
                    long nowMill=new Date().getTime();
                    long DValue=(nowMill-beginMill)/1000;
                    if(DValue%20==0){
                        String content=Constants.content[(int)DValue/20%Constants.content.length];
                        myNotify(content,(int)DValue/20);
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
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