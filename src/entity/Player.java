package entity;

import entity.Item;
import entity.Monster;
import entity.Tool;
import java.util.ArrayList;
import java.util.List;

/**
 * 玩家单例类
 * 单层entity包，无上层com.survivalgame，导包全部修正
 * 包含主动攻击怪物、4灯塔碎片通关、猴子偷窃、受伤全套逻辑
 * 完全匹配你现有的Monster、Tool类参数类型
 */
public class Player {
    // ====================== 单例模式 ======================
    private static final Player instance = new Player();
    private Player() {}
    public static Player getInstance() {
        return instance;
    }

    // ====================== 生存饱和属性（区间0~100） ======================
    private int hp;         // 血量，归0直接游戏失败
    private int hunger;     // 饥饿值，归0直接游戏失败
    private int thirst;     // 口渴值，归0直接游戏失败
    private int fatigue;    // 疲惫值，初始20，行动+3，达到100过劳死亡

    // ====================== 行动规则属性 ======================
    private int actionPoint; // 每日行动点，上限10，每次行动消耗1点
    private int day;        // 生存天数，超过30天判定任务超时失败

    // ====================== 战斗属性 ======================
    private int baseAttack;  // 玩家基础攻击力
    private int defense;     // 玩家防御力，受伤时抵消怪物伤害

    // ====================== 地图与道具 ======================
    private String currentArea; // 当前所在区域：树林/沙滩/岩石区/海边
    private List<Item> backpack;// 玩家背包，存放所有拾取的物品
    private int lighthouseFragmentCount; // 灯塔碎片收集数量，集齐4个通关

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
        this.lighthouseFragmentCount = 0;

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
        actionPoint--;
        this.fatigue = Math.min(100, this.fatigue + 3);
        System.out.println("本次行动疲惫+3，当前疲惫值：" + fatigue);
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
        actionPoint = 10;
        this.hunger = Math.max(0, this.hunger - 10);
        this.thirst = Math.max(0, this.thirst - 5);
        System.out.println("===== 第 " + day + " 天 =====");
        checkGameOver();
    }

    // ====================== 灯塔碎片收集 ======================
    /**
     * 拾取一块灯塔碎片，集齐4块直接通关
     */
    public void pickLighthouseFragment() {
        if (isGameOver || isGameWin) return;
        lighthouseFragmentCount++;
        System.out.println("获得灯塔碎片！当前碎片数量：" + lighthouseFragmentCount + "/4");
        checkGameOver();
    }

    // ====================== 玩家主动攻击怪物（适配entity包Monster、Tool） ======================
    /**
     * 玩家主动发起攻击
     * 总伤害 = 基础攻击 + 背包全部工具attackBonus加成
     * @param monster 目标怪物
     * @return true=怪物击杀成功，false=怪物存活并反击
     */
    public boolean attackMonster(Monster monster) {
        // 累加背包所有工具攻击加成
        int totalToolBonus = 0;
        for (Item item : backpack) {
            if (item instanceof Tool) {
                Tool tool = (Tool) item;
                totalToolBonus += tool.getAttackBonus();
            }
        }
        int totalDmg = this.baseAttack + totalToolBonus;
        System.out.println("你发动攻击，总伤害：" + totalDmg);

        // 怪物承受伤害
        monster.takeDamage(totalDmg);

        // 判断怪物是否死亡
        if (monster.isDead()) {
            // 怪物死亡，执行掉落逻辑
            monster.die(this);
            return true;
        } else {
            // 怪物存活，立刻反击玩家
            System.out.println(monster.getName() + " 发起反击！");
            monster.attack(this);
            return false;
        }
    }

    // ====================== 玩家受伤（怪物attack调用） ======================
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
        for (Item item : backpack) {
            if ("material".equals(item.getType())) {
                canStealList.add(item);
            }
        }
        if (canStealList.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * canStealList.size());
        return canStealList.get(randomIndex);
    }

    // ====================== 背包物品操作 ======================
    /**
     * 添加物品到背包，同名物品自动堆叠数量
     * 拾取灯塔碎片自动触发碎片计数
     * @param item 要拾取/获得的物品
     */
    public void addItem(Item item) {
        for (Item i : backpack) {
            if (i.getName().equals(item.getName())) {
                i.addCount(item.getCount());
                if ("灯塔碎片".equals(item.getName())) {
                    pickLighthouseFragment();
                }
                return;
            }
        }
        backpack.add(item);
        if ("灯塔碎片".equals(item.getName())) {
            pickLighthouseFragment();
        }
    }

    // ====================== 游戏胜负判定 ======================
    public void checkGameOver() {
        // 失败条件：疲惫满100 / 血量/饥饿/口渴归零 / 超过30天
        if (fatigue >= 100 || hp <= 0 || hunger <= 0 || thirst <= 0 || day > 30) {
            this.isGameOver = true;
            if (fatigue >= 100) {
                System.out.println("疲惫值达到100，过劳死亡！游戏结束！");
            } else if (day > 30) {
                System.out.println("30天时限已耗尽，未集齐4块灯塔碎片，任务失败！");
            } else {
                System.out.println("血量/饥饿/口渴耗尽，游戏结束！");
            }
            return;
        }
        // 胜利条件：30天内集齐4块灯塔碎片
        if (lighthouseFragmentCount >= 4) {
            this.isGameWin = true;
            System.out.println("恭喜！集齐全部4块灯塔碎片，成功通关荒岛生存！");
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

    public int getLighthouseFragmentCount() {
        return lighthouseFragmentCount;
    }
}