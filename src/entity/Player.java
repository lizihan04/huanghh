package entity;

import java.util.ArrayList;
import java.util.List;

public class Player {
    // 单例
    private static final Player instance = new Player();
    private Player() {}
    public static Player getInstance() {
        return instance;
    }

    // 生存属性
    private int hp;
    private int hunger;
    private int thirst;
    private int fatigue;

    // 行动
    private int actionPoint;
    private int day;

    // 战斗
    private int baseAttack;
    private int defense;

    private String currentArea;
    // 背包数组：固定16格，下标0~15严格对应道具顺序
    private Item[] backpackArr;

    // 灯塔碎片
    private int lighthouseFragmentCount;
    private int lighthouseProgress;
    private static final int MAX_LIGHT_FRAGMENT = 20;

    private boolean isGameOver;
    private boolean isGameWin;

    // 全部道具模板对象数组（固定顺序）
    public static final ItemData[] ALL_ITEM_ARR = {
            new ItemData("椰子", "food", "恢复饥饿", "/images/img_item/food/item_coconut.png", "hunger", 20),
            new ItemData("鱼", "food", "恢复口渴", "/images/img_item/food/item_fish.png", "thirst", 12),
            new ItemData("猪肉", "food", "恢复血量", "/images/img_item/food/item_pork.png", "hp", 30),
            new ItemData("兔肉", "food", "恢复疲惫", "/images/img_item/food/item_rabbit_meat.png", "fatigue", 22),

            new ItemData("椰子树", "material", "合成材料", "/images/img_item/material/item_coconut_tree.png"),
            new ItemData("矿石", "material", "合成材料", "/images/img_item/material/item_ore.png"),
            new ItemData("贝壳", "material", "合成材料", "/images/img_item/material/item_shell.png"),
            new ItemData("石头", "material", "合成材料", "/images/img_item/material/item_stone.png"),
            new ItemData("藤蔓", "material", "合成材料", "/images/img_item/material/item_vine.png"),
            new ItemData("木头", "material", "合成材料", "/images/img_item/material/item_wood.png"),
            new ItemData("灯塔碎片", "material", "通关道具", "/images/img_item/tool/item_tower.png"),

            new ItemData("斧头", "tool", "攻击+4，采集资源加成", "/images/img_item/tool/item_axe.png", 4, 3, 50),
            new ItemData("贝刃", "tool", "攻击+3，无采集加成", "/images/img_item/tool/item_blade.png", 3, 0, 30),
            new ItemData("锤子", "tool", "攻击+2，矿石采集加成", "/images/img_item/tool/item_hammer.png", 2, 4, 40),
            new ItemData("木棍", "tool", "攻击+1，无采集加成", "/images/img_item/tool/item_stick.png", 1, 0, 20),
            new ItemData("石剑", "tool", "攻击+8，无采集加成", "/images/img_item/tool/item_stone_sword.png", 8, 0, 35)
    };

    // 初始化
    public void initPlayer() {
        hp = 100;
        hunger = 80;
        thirst = 80;
        fatigue = 20;
        actionPoint = 10;
        day = 1;
        baseAttack = 10;
        defense = 5;
        currentArea = "沙滩";

        // 背包数组初始化16格，全部置空
        backpackArr = new Item[ALL_ITEM_ARR.length];

        lighthouseFragmentCount = 0;
        lighthouseProgress = 0;
        isGameOver = false;
        isGameWin = false;
    }

    // ===================== 行动、灯塔、战斗、受伤、偷窃 =====================
    public void doAction(String targetArea) {
        if (isGameOver || isGameWin || actionPoint <= 0) {
            System.out.println("行动力不足或游戏已结束，无法行动！");
            return;
        }
        actionPoint--;
        fatigue = Math.min(100, fatigue + 3);
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
            fatigue = Math.max(0, fatigue - 20);
            hp = Math.min(100, hp + 10);
            System.out.println("在沙滩休息，疲惫-20");
        } else {
            fatigue = Math.max(0, fatigue - 10);
            System.out.println("在" + currentArea + "休息，疲惫-10");
        }
        checkGameOver();
    }

    public void nextDay() {
        if (isGameOver || isGameWin) return;
        day++;
        actionPoint = 10;
        hunger = Math.max(0, hunger - 10);
        thirst = Math.max(0, thirst - 5);
        System.out.println("===== 第 " + day + " 天 =====");
        checkGameOver();
    }

    public void pickLighthouseFragment() {
        if (isGameOver || isGameWin) return;
        if(lighthouseFragmentCount >= MAX_LIGHT_FRAGMENT){
            System.out.println("灯塔碎片已集齐，无需再拾取！");
            return;
        }
        lighthouseFragmentCount++;
        lighthouseProgress = lighthouseFragmentCount * 5;
        System.out.println("获得灯塔碎片！当前碎片：" + lighthouseFragmentCount + "/"+MAX_LIGHT_FRAGMENT+"，建造进度：" + lighthouseProgress + "%");
        checkGameOver();
    }

    public void buildLighthouse() {
        if (isGameOver || isGameWin) return;
        if (lighthouseProgress >= 100) {
            System.out.println("灯塔已建造完成！");
            return;
        }
        lighthouseProgress += 5;
        if (lighthouseProgress > 100) lighthouseProgress = 100;
        lighthouseFragmentCount = lighthouseProgress / 5;
        System.out.println("灯塔建造进度提升，当前：" + lighthouseProgress + "%，碎片：" + lighthouseFragmentCount + "/"+MAX_LIGHT_FRAGMENT);
        checkGameOver();
    }

    public boolean attackMonster(Monster monster) {
        int totalToolBonus = 0;
        int totalCollectBonus = 0;
        for(Item item : backpackArr) {
            if(item != null && item instanceof Tool) {
                Tool t = (Tool) item;
                totalToolBonus += t.getAttackBonus();
                totalCollectBonus += t.getCollectBonus();
            }
        }
        int dmg = baseAttack + totalToolBonus;
        System.out.println("总伤害：" + dmg + "，采集加成：" + totalCollectBonus);
        monster.takeDamage(dmg);
        if(monster.isDead()) {
            monster.die(this);
            return true;
        } else {
            System.out.println(monster.getName() + "反击");
            monster.attack(this);
            return false;
        }
    }

    public int getAllCollectBonus() {
        int sum = 0;
        for(Item item : backpackArr) {
            if(item != null && item instanceof Tool) {
                sum += ((Tool)item).getCollectBonus();
            }
        }
        return sum;
    }

    public void takeDamage(int monsterAtk) {
        int real = Math.max(1, monsterAtk - defense);
        setHp(hp - real);
        System.out.println("受到" + real + "点伤害");
    }

    public Item getRandomStealableItem() {
        List<Item> list = new ArrayList<>();
        for(Item item : backpackArr) {
            if(item != null && "material".equals(item.getType())) {
                list.add(item);
            }
        }
        if(list.isEmpty()) return null;
        int idx = (int)(Math.random() * list.size());
        return list.get(idx);
    }

    // ===================== 背包核心：固定数组下标操作 =====================
    /**
     * 根据道具名称获取对应固定下标
     */
    private int getItemIndexByName(String name) {
        for(int i = 0; i < ALL_ITEM_ARR.length; i++) {
            if(ALL_ITEM_ARR[i].getItemName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 拾取道具存入对应下标格子
     */
    public void addItem(Item newItem) {
        if(isGameOver || isGameWin) {
            System.out.println("游戏结束，无法拾取");
            return;
        }
        String itemName = newItem.getName();
        int idx = getItemIndexByName(itemName);
        if(idx == -1) {
            System.out.println("非法道具，禁止存入");
            return;
        }
        // 格子已有道具：叠加数量
        if(backpackArr[idx] != null) {
            backpackArr[idx].addCount(newItem.getCount());
        } else {
            backpackArr[idx] = newItem;
        }
        // 灯塔碎片特殊计数
        if("灯塔碎片".equals(itemName)) {
            pickLighthouseFragment();
        }
        System.out.println("拾取成功：" + itemName);
    }

    /**
     * 丢弃指定数量道具
     */
    public void dropItem(Item targetItem, int dropCount) {
        if(isGameOver || isGameWin || targetItem == null || dropCount <= 0) return;
        int idx = getItemIndexByName(targetItem.getName());
        if(idx == -1 || backpackArr[idx] == null) {
            System.out.println("无该道具，丢弃失败");
            return;
        }
        Item bagItem = backpackArr[idx];
        int remain = bagItem.getCount() - dropCount;
        if(remain <= 0) {
            System.out.println("全部丢弃：" + bagItem.getName());
            backpackArr[idx] = null;
        } else {
            bagItem.setCount(remain);
            System.out.println("丢弃" + dropCount + "个" + bagItem.getName() + "，剩余" + remain);
        }
    }

    /**
     * 使用食物
     */
    public void useFood(Item foodItem) {
        if(isGameOver || isGameWin || !(foodItem instanceof Food)) {
            System.out.println("不是食物，无法使用");
            return;
        }
        Food food = (Food) foodItem;
        food.use(this);
    }

    /**
     * 按名字查找背包道具
     */
    public Item findItemByName(String itemName) {
        int idx = getItemIndexByName(itemName);
        if(idx == -1) return null;
        return backpackArr[idx];
    }

    /**
     * 清空背包所有格子
     */
    public void clearBackpack() {
        for(int i = 0; i < backpackArr.length; i++) {
            backpackArr[i] = null;
        }
        lighthouseFragmentCount = 0;
        lighthouseProgress = 0;
        System.out.println("背包全部清空");
        checkGameOver();
    }

    /**
     * 给UI返回有序道具列表（0~15固定顺序）
     */
    public List<Item> getBackpack() {
        List<Item> list = new ArrayList<>();
        for(Item item : backpackArr) {
            list.add(item);
        }
        return list;
    }

    // 胜负判定
    public void checkGameOver() {
        if (fatigue >= 100 || hp <= 0 || hunger <= 0 || thirst <= 0 || day > 30) {
            isGameOver = true;
            if(fatigue >= 100) System.out.println("过劳死亡");
            else if(day > 30) System.out.println("超过30天，未集齐"+MAX_LIGHT_FRAGMENT+"碎片");
            else System.out.println("属性耗尽，游戏结束");
            return;
        }
        if(lighthouseProgress >= 100) {
            isGameWin = true;
            System.out.println("集齐全部"+MAX_LIGHT_FRAGMENT+"灯塔碎片，通关！");
        }
    }

    // Getter Setter
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(100, hp)); checkGameOver(); }
    public int getHunger() { return hunger; }
    public void setHunger(int hunger) { this.hunger = Math.max(0, Math.min(100, hunger)); checkGameOver(); }
    public int getThirst() { return thirst; }
    public void setThirst(int thirst) { this.thirst = Math.max(0, Math.min(100, thirst)); checkGameOver(); }
    public int getFatigue() { return fatigue; }
    public void setFatigue(int fatigue) { this.fatigue = Math.max(0, Math.min(100, fatigue)); checkGameOver(); }
    public int getActionPoint() { return actionPoint; }
    public void setActionPoint(int actionPoint) { this.actionPoint = Math.max(0, Math.min(10, actionPoint)); }
    public int getDay() { return day; }
    public void setDay(int day) { this.day = Math.max(1, day); checkGameOver(); }
    public String getCurrentArea() { return currentArea; }
    public void setCurrentArea(String currentArea) { this.currentArea = currentArea; }
    public int getBaseAttack() { return baseAttack; }
    public void setBaseAttack(int baseAttack) { this.baseAttack = baseAttack; }
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = Math.max(0, defense); }
    public boolean isGameOver() { return isGameOver; }
    public boolean isGameWin() { return isGameWin; }
    public int getLighthouseFragmentCount() { return lighthouseFragmentCount; }
    public void setLighthouseFragmentCount(int count) {
        lighthouseFragmentCount = Math.max(0, Math.min(MAX_LIGHT_FRAGMENT, count));
        lighthouseProgress = lighthouseFragmentCount * 5;
        checkGameOver();
    }
    public int getLighthouseProgress() { return lighthouseProgress; }
    public void setLighthouseProgress(int progress) {
        lighthouseProgress = Math.max(0, Math.min(100, progress));
        lighthouseFragmentCount = lighthouseProgress / 5;
        checkGameOver();
    }
}