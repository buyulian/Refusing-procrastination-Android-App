package com.example.buyulian.study;


import android.os.Handler;

public class GlobalVariable {

    public volatile static int notifyCount=0;
    public volatile static int isUnlockOn =1;
    public volatile static int dayUsedTime=0;

    public volatile static long totalTime=0;
    public volatile static long oneTime=0;

    public volatile static long lockTime;
    public volatile static long unlockTime;
    public volatile static long validLockTime;
    public volatile static long validUnlockTime;
}
