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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

import static java.lang.Thread.sleep;

public class RemindService extends Service {
    private ScreenListener listener;
    private final Lock lockOne =new ReentrantLock();
    private final Lock lockAll =new ReentrantLock();
    private final Condition conditionOne= lockOne.newCondition();
    private final Condition conditionAll= lockAll.newCondition();

    private Thread singleTime=new Thread(new Runnable() {
        @Override
        public void run() {
            int gapTime=20;
            int sleepTime=gapTime*60*1000;
            long remainder=0;
            lockOne.lock();
            try {
                while (true){
                    long local=System.currentTimeMillis();
                    long andTime=GlobalVariable.oneTime+local-GlobalVariable.unlockTime;
                    long nextTime=GlobalVariable.oneCount*sleepTime;
                    remainder=nextTime-andTime;
                    while (remainder>0){
                        conditionOne.await(remainder+1000,TimeUnit.MILLISECONDS);
                        local=System.currentTimeMillis();
                        andTime=GlobalVariable.oneTime+local-GlobalVariable.unlockTime;
                        nextTime=GlobalVariable.oneCount*sleepTime;
                        remainder=nextTime-andTime;
                    }
                    int notifyTime=(int)(GlobalVariable.oneCount*gapTime*60);
                    String title="你已连续玩手机"+Tools.getTimeString(notifyTime)+",快去学习吧";
                    myNotify(title,GlobalVariable.notifyCount++);
                    GlobalVariable.oneCount++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lockOne.unlock();
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
        init();
    }

    private void init(){

        listener =new ScreenListener(this);
        long local=System.currentTimeMillis();
        GlobalVariable.unlockTime=local;
        GlobalVariable.lockTime=local;
        GlobalVariable.validLockTime=local;
        GlobalVariable.validUnlockTime=local;


        listener.register(new ScreenListener.ScreenStateListener() {

            @Override
            public void onScreenOn() {
                long local=System.currentTimeMillis();
                Tools.initDayTime(local);

                if(local-GlobalVariable.validLockTime>Constants.MIN_TIME_GAP){
                    GlobalVariable.oneTime=0;
                    GlobalVariable.oneCount=1;
                    GlobalVariable.validUnlockTime =local;
                }
                GlobalVariable.unlockTime=local;
                lockAll.unlock();
                lockOne.unlock();
//                Log.d("on","on");
            }

            @Override
            public void onScreenOff() {
                lockOne.lock();
                lockAll.lock();
//                Log.d("off","off");
                long local=System.currentTimeMillis();
                long theta=local-GlobalVariable.unlockTime;
                if(theta>Constants.MIN_ON_TIME_GAP){
                    GlobalVariable.totalTime+=theta;
                    GlobalVariable.oneTime+=theta;
                    GlobalVariable.validLockTime =local;
                }
                GlobalVariable.lockTime=local;
            }

            @Override
            public void onUserPresent() {
            }
        });

        Thread thread=new Thread(){
            @Override
            public void run() {
                int gapTime=60;
                int sleepTime=gapTime*60*1000;
                long remainder=0;
                lockAll.lock();
                try {
                    while (true){
                        long local=System.currentTimeMillis();
                        long andTime=GlobalVariable.totalTime+local-GlobalVariable.unlockTime;
                        long nextTime=GlobalVariable.totalCount*sleepTime;
                        remainder=nextTime-andTime;
                        while (remainder>0){
                            conditionAll.await(remainder+1000, TimeUnit.MILLISECONDS);
                            local=System.currentTimeMillis();
                            andTime=GlobalVariable.totalTime+local-GlobalVariable.unlockTime;
                            nextTime=GlobalVariable.totalCount*sleepTime;
                            remainder=nextTime-andTime;
                        }
                        int notifyTime=(int)(GlobalVariable.totalCount*gapTime*60);
                        String title="你今天已玩手机"+Tools.getTimeString(notifyTime)+",不要玩物丧志啊";
                        myNotify(title,GlobalVariable.notifyCount++);
                        GlobalVariable.totalCount++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lockAll.unlock();
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



    void myNotify(String content,int id){
        if(!GlobalVariable.isNotify){
            return;
        }
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = builder

                .setContentTitle(EncourageContent.getRandomContent())
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_now)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_now))
                .build();
        manager.notify(id,notification);

    }

}
