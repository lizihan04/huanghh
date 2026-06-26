package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家单例类
 * 适配GameService存档、地图、战斗逻辑
 * 同时保留lighthouseProgress进度字段兼容旧代码，底层通关逻辑以碎片为准
 * 补齐setGameMap/getGameMap、setActionPoint/setDay/setDefense等全部缺失方法
 * 攻击方法：attackMonster(Monster monster)
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
    private int actionPoint; // 每日行动点，上限10
    private int day;        // 生存天数，超过30天失败

    // ====================== 战斗属性 ======================
    private int baseAttack;  // 玩家基础攻击力
    private int defense;     // 玩家防御力，受伤减免伤害

    // ====================== 地图与道具 ======================
    private String currentArea;
    private List<Item> backpack;
    // 1. 灯塔碎片（真实通关判定）
    private int lighthouseFragmentCount;
    // 2. 灯塔进度百分比（兼容GameService旧代码，同步碎片数值）
    private int lighthouseProgress;
    // 新增地图数组，适配存档setGameMap/getGameMap
    private MapTile[][] gameMap;

    // ====================== 游戏胜负状态 ======================
    private boolean isGameOver;
    private boolean isGameWin;

    // ====================== 初始化 ======================
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
        this.lighthouseProgress = 0;
        this.gameMap = null;

        this.isGameOver = false;
        this.isGameWin = false;
    }

    // ====================== 地图存取方法（修复setGameMap/getGameMap报错） ======================
    public void setGameMap(MapTile[][] gameMap) {
        this.gameMap = gameMap;
    }
    public MapTile[][] getGameMap() {
        return gameMap;
    }

    // ====================== 行动方法 ======================
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

    public void nextDay() {
        if (isGameOver || isGameWin) return;
        day++;
        actionPoint = 10;
        this.hunger = Math.max(0, this.hunger - 10);
        this.thirst = Math.max(0, this.thirst - 5);
        System.out.println("===== 第 " + day + " 天 =====");
        checkGameOver();
    }

    // ====================== 灯塔逻辑（碎片+进度双向同步） ======================
    /**
     * 拾取碎片，同步更新进度百分比（4碎片=100进度）
     */
    public void pickLighthouseFragment() {
        if (isGameOver || isGameWin) return;
        lighthouseFragmentCount++;
        // 碎片同步换算进度：1碎片=25%
        this.lighthouseProgress = lighthouseFragmentCount * 25;
        System.out.println("获得灯塔碎片！当前碎片：" + lighthouseFragmentCount + "/4，建造进度：" + lighthouseProgress + "%");
        checkGameOver();
    }

    /**
     * 兼容旧GameService建造方法，直接增加进度，同步碎片数量
     */
    public void buildLighthouse() {
        if (isGameOver || isGameWin) return;
        if (lighthouseProgress >= 100) {
            System.out.println("灯塔已建造完成！");
            return;
        }
        lighthouseProgress += 25;
        if (lighthouseProgress > 100) lighthouseProgress = 100;
        // 进度同步换算碎片
        this.lighthouseFragmentCount = lighthouseProgress / 25;
        System.out.println("灯塔建造进度提升，当前：" + lighthouseProgress + "%，碎片：" + lighthouseFragmentCount + "/4");
        checkGameOver();
    }

    // ====================== 玩家攻击怪物（GameService里player.attack()改为player.attackMonster()） ======================
    public boolean attackMonster(Monster monster) {
        int totalToolBonus = 0;
        for (Item item : backpack) {
            if (item instanceof Tool) {
                Tool tool = (Tool) item;
                totalToolBonus += tool.getAttackBonus();
            }
        }
        int totalDmg = this.baseAttack + totalToolBonus;
        System.out.println("你发动攻击，总伤害：" + totalDmg);

        monster.takeDamage(totalDmg);

        if (monster.isDead()) {
            monster.die(this);
            return true;
        } else {
            System.out.println(monster.getName() + " 发起反击！");
            monster.attack(this);
            return false;
        }
    }

    // ====================== 受伤逻辑 ======================
    public void takeDamage(int monsterAtk) {
        int realDamage = Math.max(1, monsterAtk - this.defense);
        setHp(this.hp - realDamage);
        System.out.println("你受到 " + realDamage + " 点伤害！");
    }

    // ====================== 猴子偷窃 ======================
    public Item getRandomStealableItem() {
        List<Item> canStealList = new ArrayList<>();
        for (Item item : backpack) {
            if ("material".equals(item.getType())) {
                canStealList.add(item);
            }
        }
        if (canStealList.isEmpty()) return null;
        int randomIndex = (int) (Math.random() * canStealList.size());
        return canStealList.get(randomIndex);
    }

    // ====================== 背包操作 ======================
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

    // ====================== 胜负判定 ======================
    public void checkGameOver() {
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
        // 胜利条件：进度100% 等价于4碎片
        if (lighthouseProgress >= 100) {
            this.isGameWin = true;
            System.out.println("恭喜！灯塔建造完成，成功通关荒岛生存！");
        }
    }

    // ====================== Getter & Setter（补齐所有缺失setter） ======================
    public int getHp() { return hp; }
    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(100, hp));
        checkGameOver();
    }

    public int getHunger() { return hunger; }
    public void setHunger(int hunger) {
        this.hunger = Math.max(0, Math.min(100, hunger));
        checkGameOver();
    }

    public int getThirst() { return thirst; }
    public void setThirst(int thirst) {
        this.thirst = Math.max(0, Math.min(100, thirst));
        checkGameOver();
    }

    public int getFatigue() { return fatigue; }
    public void setFatigue(int fatigue) {
        this.fatigue = Math.max(0, Math.min(100, fatigue));
        checkGameOver();
    }

    public int getActionPoint() { return actionPoint; }
    // 新增setActionPoint 修复报错
    public void setActionPoint(int actionPoint) {
        this.actionPoint = Math.max(0, Math.min(10, actionPoint));
    }

    public int getDay() { return day; }
    // 新增setDay 修复报错
    public void setDay(int day) {
        this.day = Math.max(1, day);
        checkGameOver();
    }

    public String getCurrentArea() { return currentArea; }
    public void setCurrentArea(String currentArea) { this.currentArea = currentArea; }

    public List<Item> getBackpack() { return backpack; }

    public int getBaseAttack() { return baseAttack; }
    public void setBaseAttack(int baseAttack) { this.baseAttack = baseAttack; }

    public int getDefense() { return defense; }
    // 新增setDefense 修复报错
    public void setDefense(int defense) {
        this.defense = Math.max(0, defense);
    }

    public boolean isGameOver() { return isGameOver; }
    public boolean isGameWin() { return isGameWin; }

    // 灯塔进度、碎片全套get/set 兼容GameService
    public int getLighthouseFragmentCount() { return lighthouseFragmentCount; }
    public void setLighthouseFragmentCount(int count) {
        this.lighthouseFragmentCount = Math.max(0, Math.min(4, count));
        this.lighthouseProgress = this.lighthouseFragmentCount * 25;
        checkGameOver();
    }

    public int getLighthouseProgress() { return lighthouseProgress; }
    public void setLighthouseProgress(int progress) {
        this.lighthouseProgress = Math.max(0, Math.min(100, progress));
        this.lighthouseFragmentCount = this.lighthouseProgress / 25;
        checkGameOver();
    }
}