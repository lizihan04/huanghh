package entity;

import com.survivalgame.service.GameLogic;

import java.io.Serializable;

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
        hunger = 20;
        thirst = 20;
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
                // 椰子：降低口渴值、降低疲惫
                new Food2("椰子", 0, "thirst", 20),
                // 鱼：降低饥饿值、降低疲惫、提升血量
                new Food2("鱼", 0, "hunger", 20),
                // 猪肉：降低饥饿值、降低疲惫、提升血量
                new Food2("猪肉", 0, "hunger", 30),
                // 兔肉：降低饥饿值、降低疲惫、提升血量
                new Food2("兔肉", 0, "hunger", 20),

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

    // 统一休息入口：1=吃东西，2=原地休息
    public void eatOrRest(int choose) {
        if (GameLogic.getInstance().gameEnd()) {
            return;
        }
        if (actionPoint <= 0) {
            return;
        }
        if (choose == 1) {
            // 进入食物选择界面，UI展示4个食物按钮，分别调用下面4个进食方法
            openFoodSelectPage();
        } else if (choose == 2) {
            // 原地休息，无弹窗
            rest();
        }
    }

    // 打开食物选择弹窗/页面
    private void openFoodSelectPage() {

    }

    // ========== 4个独立进食方法，分别对应四种食物 ==========
    // 1. 吃椰子 Coco
    public void eatCoco() {
        eatSingleFood("椰子");
    }

    // 2. 吃兔肉
    public void eatRabbitMeat() {
        eatSingleFood("兔肉");
    }

    // 3. 吃鱼
    public void eatFish() {
        eatSingleFood("鱼");
    }

    // 4. 吃猪肉
    public void eatPork() {
        eatSingleFood("猪肉");
    }

    // 私有通用进食底层方法，被上面4个方法复用
    private void eatSingleFood(String foodName) {
        if (GameLogic.getInstance().gameEnd()) {
            return;
        }
        if (actionPoint <= 0) {
            return;
        }
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

        switch (foodName) {
            case "猪肉":
                hunger = Math.max(0, hunger - 30);
                fatigue = Math.max(0, fatigue - 10);
                hp = Math.min(100, hp + 15);
                break;
            case "兔肉":
                hunger = Math.max(0, hunger - 20);
                fatigue = Math.max(0, fatigue - 10);
                hp = Math.min(100, hp + 10);
                break;
            case "椰子":
                thirst = Math.max(0, thirst - 20);
                fatigue = Math.max(0, fatigue - 10);
                break;
            case "鱼":
                hunger = Math.max(0, hunger - 20);
                fatigue = Math.max(0, fatigue - 10);
                hp = Math.min(100, hp + 10);
                break;
            default:
                // 不存在的食物名称，直接不生效
                return;

        }
        // 消耗一个食物
        foodObj.setOwnCount(foodObj.getOwnCount() - 1);
        actionPoint--;
    }

    // 单纯原地休息
    public void rest() {
        if (GameLogic.getInstance().gameEnd()) {
            return;
        }
        actionPoint--;
        fatigue = Math.max(0, fatigue - 15);
    }


    // 根据物品名称查下标
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
        this.hp = Math.max(0, Math.min(100, hp));
    }

    public int getHunger() {
        return hunger;
    }
    public void setHunger(int hunger) {
        this.hunger = Math.max(0, Math.min(100, hunger));
    }

    public int getThirst() {
        return thirst;
    }
    public void setThirst(int thirst) {
        this.thirst = Math.max(0, Math.min(100, thirst));
    }

    public int getFatigue() {
        return fatigue;
    }
    public void setFatigue(int fatigue) {
        this.fatigue = Math.max(0, Math.min(100, fatigue));
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