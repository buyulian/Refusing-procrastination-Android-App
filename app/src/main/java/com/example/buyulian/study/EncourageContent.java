package com.example.buyulian.study;

import java.util.Arrays;
import java.util.Random;

public class EncourageContent {
    static public String[] content={
            "你喜欢的女生能看得上你吗？",
            "你买房了吗?",
            "延迟满足、学习复利",
            "你忘了曾经是怎么被欺负和看不起的吗?",
            "Deadline 是第一生产力",
            "没有借口，立即执行",
            "找一点借口就会落入万丈深渊",
            "拖延一点事小，破坏计划事大",
            "勿以时间短而不学习，哪怕一秒",
            "勿以时间短而浪费，积少成多",
            "方法大于努力，思考大于蛮干",
            "弱肉强食，别人才不在乎你的死活",
            "坚持不下去时，想想利益和危害",
            "你对的父母起早贪黑干脏活累活供你上学吗?",
            "你今天多努力一点，明天你就少求别人一点",
            "莫欺少年穷",
            "今日之辱，我不想再受第二次",
            "上火，感冒，不锻炼的后果就是前车之鉴",
            "今天多努力一点，明天你会轻松十点。",
            "不要等事情来了才后悔自己当初为什么不坚持",
            "怠惰是贫穷的制造厂",
            "磨炼自己的意志力，抗诱惑的能力",
            "千里之堤，溃于蚁穴",
            "今日工作不努力，明日努力找工作~",
            "有志者能使石头长出青草来",
            "经得起诱惑，耐得住寂寞，吃的了苦，才受得了王冠之重",
            "吃得苦中苦方为人上人",
            "怕吃苦的人苦一辈子，不怕吃苦的人苦一阵子",
            "此时不搏何时搏",
            "苦心人天不负，卧薪尝胆，三千越甲可吞吴",
            "一份汗水，一份收获",
            "没有伞的孩子必须努力奔跑！",
            "命运从来不会同情弱者",
            "方法大于努力，物竞天择",
            "不要以有用的借口去堕落，那只是自欺欺人而已",
            "设定的底线是一个不可逾越的，一旦越过了便万劫不复",
            "运动和卫生没有侥幸",
            "脏手不要揉眼，摸身上其他部分",
            "说话一定要三思而后行，祸从口出",
            "要考虑收益和付出来决定一件事值不值",
            "天上掉馅饼的必是骗局",
            "你的聪明还差太远，多总结",
            "每天多学一点，一年将是一个天文数字",
            "每天睡前回顾今天学了什么",
            "每天起床决定今天要做什么",
            "骄气不可长，傲骨不可无",
            "计划可以让你有条不紊",
            "居安思危，未雨绸缪",
            "人生安全最重要",
            "身体是革命的本钱",
            "事情越重要，越要注意身体，别感冒",
            "运动可以防止很多大病小病",
            "该学时尽情学，该玩时尽情玩",
            "学习编程写小说比游戏好玩",
            "无聊时想想还有很多有趣的事没做",
            "要计算利弊，不要因小失大"
    };
    private static String[] content2=content;
    static final String END_STR =";";
    public final static int[] GAP_TIME =new int[]{3,20,40,60,120,180,300,600,1200,1800,3600};
    private int cursor=0;

    static {

    }

    public String getNextContent() {
        String rs=content[cursor%content.length];
        cursor++;
        return rs;
    }

    public static void reset(){
        content=Arrays.copyOf(content2,content2.length);
    }

    public static String getRandomContent(){
        Random random=new Random(System.currentTimeMillis());
        int i=random.nextInt(content.length);
        return content[i];
    }

    public boolean isRemind(int miller){
        for(int i = 0; i< GAP_TIME.length; i++){
            if(GAP_TIME[i]==miller){
                return true;
            }
        }
        return false;
    }

    public static int getTimes(int miller){
        int ct=0;
        while (GAP_TIME[ct]<=miller){
            ct++;
        }
        return ct;
    }

    public static String getSaveStr(){
        StringBuilder sb=new StringBuilder();
        for(String str:EncourageContent.content){
            sb.append(str).append(END_STR);
        }
        return sb.toString();
    }

    public static void setSaveStr(String str){
        content=str.split(END_STR);
    }
}
