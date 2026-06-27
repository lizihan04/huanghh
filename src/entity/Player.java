package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家单例类
 * 包含完整背包底层逻辑：固定物品校验、拾取堆叠、丢弃、食用、查询、清空
 * 所有存入背包的道具强制校验，仅GameItemConfig枚举内预设固定物品可存入
 * JavaFX界面仅调用本类公开方法，不直接操作背包集合
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

    // ====================== 地图与道具背包 ======================
    private String currentArea;
    // 私有背包容器，仅存储GameItemConfig定义的固定合法物品
    private List<Item> backpack;
    // 1. 灯塔碎片（真实通关判定）
    private int lighthouseFragmentCount;
    // 2. 灯塔进度百分比（兼容旧代码，和碎片双向同步）
    private int lighthouseProgress;
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

    // ====================== 地图存取 ======================
    public void setGameMap(MapTile[][] gameMap) {
        this.gameMap = gameMap;
    }
    public MapTile[][] getGameMap() {
        return gameMap;
    }

    // ====================== 行动相关方法 ======================
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

    // ====================== 灯塔碎片同步逻辑 ======================
    /**
     * 拾取灯塔碎片，同步更新进度
     */
    public void pickLighthouseFragment() {
        if (isGameOver || isGameWin) return;
        lighthouseFragmentCount++;
        lighthouseProgress = lighthouseFragmentCount * 25;
        System.out.println("获得灯塔碎片！当前碎片：" + lighthouseFragmentCount + "/4，建造进度：" + lighthouseProgress + "%");
        checkGameOver();
    }

    public void buildLighthouse() {
        if (isGameOver || isGameWin) return;
        if (lighthouseProgress >= 100) {
            System.out.println("灯塔已建造完成！");
            return;
        }
        lighthouseProgress += 25;
        if (lighthouseProgress > 100) lighthouseProgress = 100;
        lighthouseFragmentCount = lighthouseProgress / 25;
        System.out.println("灯塔建造进度提升，当前：" + lighthouseProgress + "%，碎片：" + lighthouseFragmentCount + "/4");
        checkGameOver();
    }

    // ====================== 战斗相关 ======================
    public boolean attackMonster(Monster monster) {
        int totalToolBonus = 0;
        int totalCollectBonus = 0;
        for (Item item : backpack) {
            if (item instanceof Tool) {
                Tool tool = (Tool) item;
                totalToolBonus += tool.getAttackBonus();
                totalCollectBonus += tool.getCollectBonus();
            }
        }
        int totalDmg = this.baseAttack + totalToolBonus;
        System.out.println("你发动攻击，总伤害：" + totalDmg);
        System.out.println("当前背包全部工具采集加成：" + totalCollectBonus);

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

    /**
     * 获取背包内全部工具总采集加成（底层采集逻辑使用）
     */
    public int getAllCollectBonus() {
        int sumBonus = 0;
        for (Item item : backpack) {
            if (item instanceof Tool) {
                sumBonus += ((Tool) item).getCollectBonus();
            }
        }
        return sumBonus;
    }

    // ====================== 受伤扣血 ======================
    public void takeDamage(int monsterAtk) {
        int realDamage = Math.max(1, monsterAtk - this.defense);
        setHp(this.hp - realDamage);
        System.out.println("你受到 " + realDamage + " 点伤害！");
    }

    // ====================== 猴子偷窃逻辑 ======================
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

    // ====================== 背包底层核心逻辑（JavaFX界面调用入口） ======================
    /**
     * 拾取物品存入背包
     * 底层强制校验：仅GameItemConfig预设固定物品允许存入，拦截自定义道具
     * 同名道具自动堆叠数量，拾取灯塔碎片自动同步计数
     * @param item 待存入的道具对象
     */
    public void addItem(Item item) {
        // 校验是否为系统规定的合法固定物品
        boolean legalItem = false;
        for (GameItemConfig cfg : GameItemConfig.values()) {
            if (cfg.getName().equals(item.getName())) {
                legalItem = true;
                break;
            }
        }
        if (!legalItem) {
            System.out.println("底层拦截：非法自定义道具【" + item.getName() + "】禁止存入背包");
            return;
        }

        // 同名道具堆叠
        for (Item bagItem : backpack) {
            if (bagItem.getName().equals(item.getName())) {
                bagItem.addCount(item.getCount());
                if ("灯塔碎片".equals(item.getName())) {
                    pickLighthouseFragment();
                }
                return;
            }
        }
        // 无同名则新增
        backpack.add(item);
        if ("灯塔碎片".equals(item.getName())) {
            pickLighthouseFragment();
        }
    }

    /**
     * 丢弃指定数量道具
     * @param targetItem 背包内目标道具
     * @param dropCount 丢弃数量
     */
    public void dropItem(Item targetItem, int dropCount) {
        if (isGameOver || isGameWin || targetItem == null || dropCount <= 0) {
            return;
        }
        // 遍历背包
        for (Item bagItem : backpack) {
            if (bagItem.getName().equals(targetItem.getName())) {
                int remainNum = bagItem.getCount() - dropCount;
                if (remainNum <= 0) {
                    // 剩余数量不足，全部移除
                    backpack.remove(bagItem);
                    System.out.println("丢弃：全部移除【" + bagItem.getName() + "】");
                } else {
                    // 扣除对应数量
                    bagItem.setCount(remainNum);
                    // 这里改为 bagItem.getName()，消除红色报错
                    System.out.println("丢弃" + dropCount + "个" + bagItem.getName() + "，剩余数量：" + remainNum);
                }
                return;
            }
        }
        // 循环走完没找到对应道具
        System.out.println("背包不存在该道具，丢弃失败");
    }

    /**
     * 使用食物，自动调用Food重写的use方法，扣数量+恢复生存属性
     * @param foodItem 背包内食物道具
     */
    public void useFood(Item foodItem) {
        if (isGameOver || isGameWin || !(foodItem instanceof Food)) {
            System.out.println("使用失败：该物品不是食物");
            return;
        }
        Food food = (Food) foodItem;
        food.use(this);
    }

    /**
     * 根据道具名称查询背包内物品
     * @param itemName 道具名称（必须是GameItemConfig内定义名称）
     * @return 找到返回道具，无则返回null
     */
    public Item findItemByName(String itemName) {
        for (Item bagItem : backpack) {
            if (bagItem.getName().equals(itemName)) {
                return bagItem;
            }
        }
        return null;
    }

    /**
     * 清空整个背包，同步重置灯塔碎片与进度
     */
    public void clearBackpack() {
        backpack.clear();
        lighthouseFragmentCount = 0;
        lighthouseProgress = 0;
        System.out.println("背包已全部清空，灯塔碎片清零");
        checkGameOver();
    }

    /**
     * 对外提供背包只读数据副本（JavaFX界面渲染专用）
     * 外部无法直接修改内部原始背包集合，所有增删改只能通过本类方法
     * @return 背包物品列表副本
     */
    public List<Item> getBackpack() {
        return new ArrayList<>(backpack);
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
        if (lighthouseProgress >= 100) {
            this.isGameWin = true;
            System.out.println("恭喜！灯塔建造完成，成功通关荒岛生存！");
        }
    }

    // ====================== Getter & Setter ======================
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
    public void setActionPoint(int actionPoint) {
        this.actionPoint = Math.max(0, Math.min(10, actionPoint));
    }

    public int getDay() { return day; }
    public void setDay(int day) {
        this.day = Math.max(1, day);
        checkGameOver();
    }

    public String getCurrentArea() { return currentArea; }
    public void setCurrentArea(String currentArea) { this.currentArea = currentArea; }

    public int getBaseAttack() { return baseAttack; }
    public void setBaseAttack(int baseAttack) { this.baseAttack = baseAttack; }

    public int getDefense() { return defense; }
    public void setDefense(int defense) {
        this.defense = Math.max(0, defense);
    }

    public boolean isGameOver() { return isGameOver; }
    public boolean isGameWin() { return isGameWin; }

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