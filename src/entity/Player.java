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
    private int hp;//血量
    private int hunger;//饥饿
    private int thirst;//口渴
    private int fatigue;//疲惫
    private int actionPoint;//行动点
    private int day;//生存天数
    private int baseAttack;//攻击
    private int defense;//防御

    private String currentArea; //所在的地区
    private Item[] backpackArr; //背包

    // 灯塔碎片 上限20
    private int lighthouseFragmentCount;//已收集碎片数量
    private int lighthouseProgress;// 建造进度（0~100%）
    private static final int MAX_LIGHT_FRAGMENT = 20;//通关需要20块碎片

    private boolean isGameOver;
    private boolean isGameWin;

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

        // 背包数组初始化，初始全部是0
        backpackArr = new Item[]{
                //食物food（0~3）：new Item(名称,type,描述,图片,恢复类型,数值,初始数量0)
                // 0 椰子 food
                new Item("椰子", "food", "恢复饥饿", "/images/img_item/food/item_coconut.png", "hunger", 20, 0),
                // 1 鱼 food 减疲惫
                new Item("鱼", "food", "减少疲惫", "/images/img_item/food/item_fish.png", "fatigue", 12, 0),
                // 2 猪肉 food 回血
                new Item("猪肉", "food", "恢复血量", "/images/img_item/food/item_pork.png", "hp", 30, 0),
                // 3 兔肉 food 回饥饿
                new Item("兔肉", "food", "恢复饥饿", "/images/img_item/food/item_rabbit_meat.png", "hunger", 22, 0),

                // 材料material（4~9）：new Item(名称,type,描述,图片,初始数量0)                        // 4 矿石 material
                new Item("矿石", "material", "合成材料", "/images/img_item/material/item_ore.png", 0),
                // 5 贝壳
                new Item("贝壳", "material", "合成材料", "/images/img_item/material/item_shell.png", 0),
                // 6 石头
                new Item("石头", "material", "合成材料", "/images/img_item/material/item_stone.png", 0),
                // 7 藤蔓
                new Item("藤蔓", "material", "合成材料", "/images/img_item/material/item_vine.png", 0),
                // 8 木头
                new Item("木头", "material", "合成材料", "/images/img_item/material/item_wood.png", 0),
                // 9 灯塔碎片
                new Item("灯塔碎片", "material", "通关道具", "/images/img_item/tool/item_tower.png", 0),

                // 工具tool（10~14）：new Item(名称,type,描述,图片,攻击加成,最大耐久,初始数量0
                // 10 斧头 tool
                new Item("斧头", "tool", "攻击+4", "/images/img_item/tool/item_axe.png", 4, 50, 0),
                // 11 贝刃
                new Item("贝刃", "tool", "攻击+3", "/images/img_item/tool/item_blade.png", 3, 30, 0),
                // 12 锤子
                new Item("锤子", "tool", "攻击+2", "/images/img_item/tool/item_hammer.png", 2, 40, 0),
                // 13 木棍
                new Item("木棍", "tool", "攻击+1", "/images/img_item/tool/item_stick.png", 1, 20, 0),
                // 14 石剑
                new Item("石剑", "tool", "攻击+8", "/images/img_item/tool/item_stone_sword.png", 8, 35, 0)
        };

        lighthouseFragmentCount = 0;
        lighthouseProgress = 0;
        isGameOver = false;
        isGameWin = false;
    }

    // 移动行动
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

    // 休息统一疲惫-10
    public void rest() {
        if (isGameOver || isGameWin || actionPoint <= 0) {
            System.out.println("行动力不足，无法休息！");
            return;
        }
        actionPoint--;
        fatigue -= 10;
        if (fatigue < 0)
            fatigue = 0;
        System.out.println("休息完毕，疲惫减少10，当前疲惫值：" + fatigue);
        checkGameOver();
    }

    // 新一天
    public void nextDay() {
        if (isGameOver || isGameWin) return;
        day++;
        actionPoint = 10;
        System.out.println("===== 第 " + day + " 天 =====");
        checkGameOver();
    }

    /**拾取灯塔碎片 上限20
    public void pickLighthouseFragment() {
        if (isGameOver || isGameWin) return;
        if (lighthouseFragmentCount >= MAX_LIGHT_FRAGMENT) {
            System.out.println("灯塔碎片已集齐，无需再拾取！");
            return;
        }
        lighthouseFragmentCount++;
        lighthouseProgress = lighthouseFragmentCount * 5;
        System.out.println("获得灯塔碎片！当前碎片：" + lighthouseFragmentCount + "/" + MAX_LIGHT_FRAGMENT + "，建造进度：" + lighthouseProgress + "%");
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
        System.out.println("灯塔建造进度提升，当前：" + lighthouseProgress + "%，碎片：" + lighthouseFragmentCount + "/" + MAX_LIGHT_FRAGMENT);
        checkGameOver();
    }*/

    // 攻击怪物
    public boolean attackMonster(Monster monster) {
        int totalToolAtk = 0;//
        for (Item item : backpackArr) {
            if (item.getOwnCount() > 0 && "tool".equals(item.getItemType())) {
                totalToolAtk += item.getAttackBonus();
            }
        }
        int totalDmg = baseAttack + totalToolAtk;
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

    // 受伤
    public void takeDamage(int monsterAtk) {
        int realDmg = Math.max(1, monsterAtk - defense);
        setHp(hp - realDmg);
        System.out.println("你受到 " + realDmg + " 点伤害！");
    }

    // 猴子偷材料
    public Item getRandomStealableItem() {
        List<Item> list = new ArrayList<>();
        for (Item item : backpackArr) {
            if ("material".equals(item.getItemType()) && item.getOwnCount() > 0) {
                list.add(item);
            }
        }
        if (list.isEmpty()) return null;
        int idx = (int) (Math.random() * list.size());
        return list.get(idx);
    }

    // 根据物品名取下标
    private int getItemIndexByName(String itemName) {
        for (int i = 0; i < backpackArr.length; i++) {
            if (backpackArr[i].getItemName().equals(itemName)) {
                return i;
            }
        }
        return -1;
    }

    // 拾取物品（Monster死亡掉落调用此方法）
    public void addItem(String itemName, int num) {
        if (isGameOver || isGameWin) {
            System.out.println("游戏已结束，无法拾取物品");
            return;
        }
        int idx = getItemIndexByName(itemName);
        if (idx == -1) {
            System.out.println("非法道具，禁止存入背包");
            return;
        }
        Item target = backpackArr[idx];
        target.addCount(num);
        System.out.println("拾取成功：" + itemName + " x" + num);
        /**if ("灯塔碎片".equals(itemName)) {
         * pickLighthouseFragment();
        }*/
    }

    // 丢弃物品
    public void dropItem(String itemName, int dropCount) {
        if (isGameOver || isGameWin || dropCount <= 0) return;
        int idx = getItemIndexByName(itemName);
        if (idx == -1) {
            System.out.println("背包不存在该道具，丢弃失败");
            return;
        }
        Item target = backpackArr[idx];
        if (target.getOwnCount() < dropCount) {
            System.out.println("物品数量不足，无法丢弃");
            return;
        }
        target.reduceCount(dropCount);
        System.out.println("丢弃" + dropCount + "个" + itemName + "，剩余持有：" + target.getOwnCount());
    }

    // 使用食物
    public void useFood(String foodName) {
        if (isGameOver || isGameWin) return;
        int idx = getItemIndexByName(foodName);
        if (idx == -1) {
            System.out.println("无该食物");
            return;
        }
        Item food = backpackArr[idx];
        if (!"food".equals(food.getItemType())) {
            System.out.println("该物品不是食物，无法使用");
            return;
        }
        if (food.getOwnCount() <= 0) {
            System.out.println(foodName + "数量不足");
            return;
        }
        String recoverType = food.getRecoverType();
        int val = food.getRecoverValue();
        switch (recoverType) {
            case "hp":
                setHp(hp + val);
                System.out.println("食用" + foodName + "，恢复血量" + val);
                break;
            case "hunger":
                setHunger(hunger + val);
                System.out.println("食用" + foodName + "，恢复饥饿" + val);
                break;
            case "fatigue":
                fatigue = Math.max(0, fatigue - val);
                System.out.println("食用" + foodName + "，减少疲惫" + val);
                break;
            case "thirst":
                setThirst(thirst + val);
                System.out.println("食用" + foodName + "，恢复口渴" + val);
                break;
        }
        food.reduceCount(1);
        checkGameOver();
    }

    public Item findItemByName(String itemName) {
        int idx = getItemIndexByName(itemName);
        if (idx == -1) return null;
        return backpackArr[idx];
    }

    public void clearBackpack() {
        for (Item item : backpackArr) {
            item.setOwnCount(0);
        }
        lighthouseFragmentCount = 0;
        lighthouseProgress = 0;
        System.out.println("背包已全部清空，灯塔碎片清零");
        checkGameOver();
    }

    public Item[] getBackpack() {
        return backpackArr;
    }

    // 胜负判定
    public void checkGameOver() {
        if (fatigue >= 100 || hp <= 0 || hunger <= 0 || thirst <= 0 || day > 30) {
            isGameOver = true;
            if (fatigue >= 100) {
                System.out.println("疲惫值达到100，过劳死亡！游戏结束！");
            } else if (day > 30) {
                System.out.println("30天时限已耗尽，未集齐" + MAX_LIGHT_FRAGMENT + "块灯塔碎片，任务失败！");
            } else {
                System.out.println("血量/饥饿/口渴耗尽，游戏结束！");
            }
            return;
        }
        if (lighthouseProgress >= 100) {
            isGameWin = true;
            System.out.println("恭喜！集齐全部" + MAX_LIGHT_FRAGMENT + "块灯塔碎片，建造完成，成功通关荒岛生存！");
        }
    }

    // Getter & Setter
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

    public int getMaxLightFragment() {
        return MAX_LIGHT_FRAGMENT;
    }
}