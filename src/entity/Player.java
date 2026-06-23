package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家单例类
 * 存储玩家全部生存、战斗、背包、游戏进度数据
 * 提供行动、休息、建造、受伤、物品偷窃配套方法
 */
public class Player {
    // ====================== 单例模式 ======================
    // 全局唯一玩家实例
    private static final Player instance = new Player();

    // 私有构造，禁止外部new创建对象
    private Player() {}

    // 获取全局唯一玩家对象
    public static Player getInstance() {
        return instance;
    }

    private int hp;         // 血量
    private int hunger;     // 饥饿值
    private int thirst;     // 口渴值
    private int fatigue;    // 疲惫值
    private int actionPoint; // 每日行动点
    private int day;        // 生存天数

    private int baseAttack;  // 玩家基础攻击力
    private int defense;     // 玩家防御力

    private String currentArea; // 当前所在区域：树林/沙滩/岩石区/海边
    private List<Item> backpack;// 玩家背包
    private int lighthouseProgress; // 灯塔建造进度

    // ====================== 游戏胜负状态 ======================
    private boolean isGameOver; // 游戏失败标记
    private boolean isGameWin;  // 游戏胜利标记

    // ====================== 初始化玩家所有初始数据 ======================
    public void initPlayer() {
        this.hp = 100;
        this.hunger = 80;
        this.thirst = 80;
        this.fatigue = 20;

        this.actionPoint = 10;
        this.day = 1;

        this.baseAttack = 10;
        this.defense = 5;

        this.currentArea = "沙滩";
        this.backpack = new ArrayList<>();
        this.lighthouseProgress = 0;

        this.isGameOver = false;
        this.isGameWin = false;
    }

    // ====================== 行动相关方法 ======================
    /**
     * 执行移动/采集等一次行动
     * @param targetArea 目标区域
     */
    public void doAction(String targetArea) {
        if (isGameOver || isGameWin || actionPoint <= 0) {
            System.out.println("行动力不足或游戏已结束，无法行动！");
            return;
        }
        // 消耗行动点
        actionPoint--;
        // 行动增加疲惫值
        this.fatigue = Math.min(100, this.fatigue + 3);
        System.out.println("本次行动疲惫+3，当前疲惫值：" + fatigue);
        // 行动后检查是否触发死亡
        checkGameOver();
    }

    /**
     * 原地休息，恢复血量、降低疲惫
     */
    public void rest() {
        if (isGameOver || isGameWin || actionPoint <= 0) {
            System.out.println("行动力不足，无法休息！");
            return;
        }
        actionPoint--;
        if ("沙滩".equals(currentArea)) {
            this.fatigue = Math.max(0, this.fatigue - 20);
            this.hp = Math.min(100, this.hp + 10);
            System.out.println("在沙滩休息，疲惫-20，血量+10");
        } else {
            this.fatigue = Math.max(0, this.fatigue - 10);
            System.out.println("在" + currentArea + "休息，疲惫-10");
        }
        checkGameOver();
    }

    /**
     * 切换至下一天，重置行动点，扣除每日饥饿口渴消耗
     */
    public void nextDay() {
        if (isGameOver || isGameWin) return;
        day++;
        // 超过30天直接判定失败
        if (day > 30) {
            checkGameOver();
            return;
        }
        actionPoint = 10;
        this.hunger = Math.max(0, this.hunger - 10);
        this.thirst = Math.max(0, this.thirst - 5);
        System.out.println("===== 第 " + day + " 天 =====");
        checkGameOver();
    }

    // ====================== 灯塔建造 ======================
    /**
     * 消耗材料建造灯塔，每次提升20%进度
     */
    public void buildLighthouse() {
        if (isGameOver || isGameWin) return;
        lighthouseProgress += 20;
        lighthouseProgress = Math.min(100, lighthouseProgress);
        System.out.println("灯塔建造进度：" + lighthouseProgress + "%");
        checkGameOver();
    }

    // ====================== 受伤系统（猴子、鲨鱼等怪物攻击调用） ======================
    /**
     * 受到怪物攻击，计算防御减伤后扣血
     * @param monsterAtk 怪物原始攻击力
     */
    public void takeDamage(int monsterAtk) {
        // 最低造成1点伤害，防御无法完全免伤
        int realDamage = Math.max(1, monsterAtk - this.defense);
        setHp(this.hp - realDamage);
        System.out.println("你受到 " + realDamage + " 点伤害！");
    }

    // ====================== 猴子偷窃配套方法 ======================
    /**
     * 随机获取背包中一件物资材料（仅material类型可被猴子偷走）
     * @return 随机材料，无材料返回null
     */
    public Item getRandomStealableItem() {
        List<Item> canStealList = new ArrayList<>();
        // 筛选所有材料类物品
        for (Item item : backpack) {
            if ("material".equals(item.getType())) {
                canStealList.add(item);
            }
        }
        // 背包无材料返回null
        if (canStealList.isEmpty()) {
            return null;
        }
        // 随机下标取出物品
        int randomIndex = (int) (Math.random() * canStealList.size());
        return canStealList.get(randomIndex);
    }

    // ====================== 背包物品操作 ======================
    /**
     * 添加物品到背包，同名物品自动堆叠数量
     * @param item 要拾取/获得的物品
     */
    public void addItem(Item item) {
        for (Item i : backpack) {
            if (i.getName().equals(item.getName())) {
                i.addCount(item.getCount());
                return;
            }
        }
        backpack.add(item);
    }

    // ====================== 游戏胜负判定 ======================
    /**
     * 统一判定游戏胜利/失败条件
     */
    public void checkGameOver() {
        // 失败条件：疲惫满100 / 血量/饥饿/口渴归零
        if (fatigue >= 100 || hp <= 0 || hunger <= 0 || thirst <= 0) {
            this.isGameOver = true;
            if (fatigue >= 100) {
                System.out.println("疲惫值达到100，过劳死亡！游戏结束！");
            } else {
                System.out.println("游戏结束！未在规定时间内完成任务");
            }
            return;
        }
        // 胜利条件：30天内灯塔建造进度100%
        if (day <= 30 && lighthouseProgress >= 100) {
            this.isGameWin = true;
            System.out.println("恭喜！成功在时限内建造完灯塔，游戏通关！");
        }
    }

    // ====================== Getter & Setter 存取方法 ======================
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(100, hp));
        checkGameOver();
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = Math.max(0, Math.min(100, hunger));
        checkGameOver();
    }

    public int getThirst() {
        return thirst;
    }

    public void setThirst(int thirst) {
        this.thirst = Math.max(0, Math.min(100, thirst));
        checkGameOver();
    }

    public int getFatigue() {
        return fatigue;
    }

    public void setFatigue(int fatigue) {
        this.fatigue = Math.max(0, Math.min(100, fatigue));
        checkGameOver();
    }

    public int getActionPoint() {
        return actionPoint;
    }

    public int getDay() {
        return day;
    }

    public String getCurrentArea() {
        return currentArea;
    }

    public void setCurrentArea(String currentArea) {
        this.currentArea = currentArea;
    }

    public List<Item> getBackpack() {
        return backpack;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    public int getDefense() {
        return defense;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isGameWin() {
        return isGameWin;
    }

    public int getLighthouseProgress() {
        return lighthouseProgress;
    }
}