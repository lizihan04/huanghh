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

    private Item2[] backpackArr;//背包

    //初始化
    public void initPlayer() {
        hp = 100;
        hunger = 80;
        thirst = 80;
        fatigue = 20;
        actionPoint = 10;
        day = 1;
        fragment = 0;
        baseAttack = 10;
        backpackArr = new Item2[]{
                // ========== 食物 Food2(String名称, 初始数量0, 恢复类型, 恢复数值) ==========
                new Food2("椰子", 0, "hunger", 20),
                new Food2("鱼", 0, "fatigue", 12),
                new Food2("猪肉", 0, "hp", 30),
                new Food2("兔肉", 0, "hunger", 22),

                // ========== 材料 Material2(String名称, 初始数量0) ==========
                new Material2("矿石", 0),
                new Material2("贝壳", 0),
                new Material2("石头", 0),
                new Material2("藤蔓", 0),
                new Material2("木头", 0),
                new Material2("灯塔碎片", 0),

                // ========== 工具 Tool2(String名称, 初始数量0, 攻击加成) ==========
                new Tool2("斧头", 0, 4),
                new Tool2("贝刃", 0, 3),
                new Tool2("锤子", 0, 2),
                new Tool2("木棍", 0, 1),
                new Tool2("石剑", 0, 8)
        };
    }
    //休息
    public void rest(){}
    public void eat(){}
    //下一天
    public void next_day(){}
    //工作台
}
