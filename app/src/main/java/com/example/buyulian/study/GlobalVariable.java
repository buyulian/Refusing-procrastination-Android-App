package com.example.buyulian.study;


import android.os.Handler;

import java.util.Date;

public class GlobalVariable {

    public volatile static int notifyCount=0;

    public volatile static long startUpDay=-1;

    public volatile static long totalTime=0;
    public volatile static long totalCount=1;
    public volatile static long oneTime=0;
    public volatile static long oneCount=1;

    public volatile static long lockTime;
    public volatile static long unlockTime;
    public volatile static long validLockTime;
    public volatile static long validUnlockTime;

    public volatile static boolean isNotify=true;
}
