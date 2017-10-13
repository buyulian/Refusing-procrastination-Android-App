package com.example.buyulian.study;

public class EncourageContent {
    static public String[] content={
            "拖一秒无尽深渊",
            "不努力你拿什么和别人比",
            "你不努力，你的女神就要嫁给别人了",
            "此时不吃苦，将来会付出更多",
            "莫欺少年穷",
            "不要让人看不起",
            "今日之辱，我不想再受第二次",
            "上火，感冒，不锻炼的后果就是前车之鉴"
    };
    static String endStr=";";
    private int[] gapTime=new int[]{0,20,40,60,120,180,300,600,1200,1800,3600};
    private int cursor=0;

    public String getNextContent() {
        String rs=content[cursor%content.length];
        cursor++;
        return rs;
    }

    public boolean isRemind(int miller){
        for(int i=0;i<gapTime.length;i++){
            if(gapTime[i]==miller){
                return true;
            }
        }
        return false;
    }

    public static String getSaveStr(){
        StringBuilder sb=new StringBuilder();
        for(String str:EncourageContent.content){
            sb.append(str).append(endStr);
        }
        return sb.toString();
    }

    public static void setSaveStr(String str){
        content=str.split(endStr);
    }
}
