package entity;

import com.survivalgame.service.GameLogic;

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

    private Item2[] backpackArr;//背包仅作为物品存储容器，不封装任何增减业务

    //初始化：一次性创建所有物品对象存入背包数组
    public void initPlayer() {
        hp = 100;
        hunger = 80;
        thirst = 80;
        fatigue = 20;
        actionPoint = 10;
        day = 1;
        fragment = 0;
        baseAttack = 10;
        space = "";
        isGameOver = false;
        isGameWin = false;

        backpackArr = new Item2[]{
                // ========== 食物 Food2(名称, 初始数量, 恢复类型, 恢复数值) ==========
                new Food2("椰子", 0, "hunger", 20),
                new Food2("鱼", 0, "fatigue", 12),
                new Food2("猪肉", 0, "hp", 30),
                new Food2("兔肉", 0, "hunger", 22),

                // ========== 材料 Material2(名称, 初始数量) ==========
                new Material2("矿石", 0),
                new Material2("贝壳", 0),
                new Material2("石头", 0),
                new Material2("藤蔓", 0),
                new Material2("木头", 0),
                new Material2("灯塔碎片", 0),

                // ========== 工具 Tool2(名称, 初始数量, 攻击加成) ==========
                new Tool2("斧头", 0, 4),
                new Tool2("贝刃", 0, 3),
                new Tool2("锤子", 0, 2),
                new Tool2("木棍", 0, 1),
                new Tool2("石剑", 0, 8)
        };
    }

    // 统一休息入口：1=吃东西，2=原地休息，无独立eat方法
    public void eatOrRest(int choose, String foodName) {
        if (GameLogic.getInstance().gameEnd()) {
            return;
        }
        if (actionPoint <= 0) {
            return;
        }
        if (choose == 1) {
            // 仅查询下标，数量修改直接操作物品对象，背包不封装增减逻辑
            int targetIndex = getItemIndexByName(foodName);
            if (targetIndex == -1) {
                return;
            }
            Item2 targetItem = backpackArr[targetIndex];
            if (!(targetItem instanceof Food2)) {
                return;
            }
            Food2 foodObj = (Food2) targetItem;
            if (foodObj.getOwnCount() <= 0) {
                return;
            }

            // 修改玩家生存属性
            String recoverType = foodObj.getRecoverType();
            int recoverNum = foodObj.getRecoverValue();
            switch (recoverType) {
                case "hp":
                    hp = Math.min(100, hp + recoverNum);
                    break;
                case "hunger":
                    hunger = Math.min(100, hunger + recoverNum);
                    break;
                case "thirst":
                    thirst = Math.min(100, thirst + recoverNum);
                    break;
                case "fatigue":
                    fatigue = Math.max(0, fatigue - recoverNum);
                    break;
                default:
                    return;
            }
            // 直接调用物品自身setOwnCount修改数量，背包不处理增减
            foodObj.setOwnCount(foodObj.getOwnCount() - 1);
            actionPoint--;
        } else if (choose == 2) {
            rest();
        }
    }

    // 单纯原地休息
    public void rest() {
        if (GameLogic.getInstance().gameEnd()) {
            return;
        }
        actionPoint--;
        fatigue = Math.max(0, fatigue - 10);
    }

    // 下一天
    public void next_day() {
        if (GameLogic.getInstance().gameEnd()) {
            return;
        }
        day++;
        actionPoint = 10;
        hunger = Math.max(0, hunger - 10);
        thirst = Math.max(0, thirst - 5);
    }

    // 仅保留私有工具：根据物品名称查询背包下标，无任何增减封装方法
    private int getItemIndexByName(String itemName) {
        for (int i = 0; i < backpackArr.length; i++) {
            if (backpackArr[i].getItemName().equals(itemName)) {
                return i;
            }
        }
        return -1;
    }

    // ===================== 全部属性 Getter & Setter =====================
    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHunger() {
        return hunger;
    }
    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getThirst() {
        return thirst;
    }
    public void setThirst(int thirst) {
        this.thirst = thirst;
    }

    public int getFatigue() {
        return fatigue;
    }
    public void setFatigue(int fatigue) {
        this.fatigue = fatigue;
    }

    public int getActionPoint() {
        return actionPoint;
    }
    public void setActionPoint(int actionPoint) {
        this.actionPoint = actionPoint;
    }

    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }

    public int getFragment() {
        return fragment;
    }
    public void setFragment(int fragment) {
        this.fragment = fragment;
    }

    public int getBaseAttack() {
        return baseAttack;
    }
    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    public String getSpace() {
        return space;
    }
    public void setSpace(String space) {
        this.space = space;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public boolean isGameWin() {
        return isGameWin;
    }
    public void setGameWin(boolean gameWin) {
        isGameWin = gameWin;
    }

    public Item2[] getBackpackArr() {
        return backpackArr;
    }
    public void setBackpackArr(Item2[] backpackArr) {
        this.backpackArr = backpackArr;
    }

    public static int getMAX_FRAGMENT() {
        return MAX_FRAGMENT;
    }
}