package com.example.buyulian.study;

public class Tools {
    public static String getTimeString(int seconds){
        if(seconds==0){
            return " 0 分钟";
        }
        int hours=seconds/3600;
        int minute=seconds/60%60;
        int second=seconds%60;
        StringBuilder sb=new StringBuilder();
        if(hours!=0){
            sb.append(" ").append(hours).append(" 小时");
        }
        if(minute!=0){
            sb.append(" ").append(minute).append(" 分钟");
        }
        if(second!=0){
            sb.append(" ").append(second).append(" 秒");
        }
        return sb.toString();
    }
}
