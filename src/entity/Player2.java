package entity;

public class Player2 {
    // 单例
    private static final Player2 instance = new Player2();
    private Player2() {}
    public static Player2 getInstance() {
        return instance;
    }
    private static final int MAX_FRAGMENT = 20;//通关需要20块碎片
    //玩家属性（总）
    private int hp;//血量
    private int hunger;//饥饿
    private int thirst;//口渴
    private int fatigue;//疲惫
    private int actionPoint;//行动点
    private int day;//生存天数
    private int fragment;//碎片
    private int baseAttack;//玩家基础战斗力
    private String space;//玩家所处地图

    //是否结束游戏
    private boolean isGameOver;
    private boolean isGameWin;

    private Item[] backpackArr; //背包

    //玩家方法（总）
    //()
    //初始化
    public void initPlayer2(){}
    //休息
    public void rest(){}
    public void eat(){}
    //下一天
    public void next_day(){}
    //工作台
    public void make(){}
    //

    //getter() & setter()
    public Item[] getBackpackArr(){
        return this.backpackArr;
    }
}
