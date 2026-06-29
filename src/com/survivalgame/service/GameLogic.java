package com.survivalgame.service;

import entity.Boar2;
import entity.Fish2;
import entity.Hare2;
import entity.Item2;
import entity.Monster2;
import entity.Player2;
import java.util.Random;

import com.survivalgame.util.FileUtil2;

public class GameLogic {
    // 单例模式
    private static final GameLogic instance = new GameLogic();
    private GameLogic() {}
    public static GameLogic getInstance() {
        return instance;
    }

    // 全局玩家对象
    Player2 player = Player2.getInstance();
    private final Random random = new Random();
    private static final int MAX_FRAGMENT = 20;

    // 当前遭遇的怪物，用于武器选择战斗
    private Monster2 currentMonster;

    public Monster2 getCurrentMonster() {
        return currentMonster;
    }

    public void clearCurrentMonster() {
        this.currentMonster = null;
    }

    public String fightMonsterWithWeapon(String weaponName) {
        if (currentMonster == null) {
            return "当前没有需要战斗的怪物。";
        }

        int weaponBonus = 0;
        if (!"徒手".equals(weaponName)) {
            Item2[] backpack = player.getBackpackArr();
            entity.Tool2 chosenTool = null;
            for (Item2 item : backpack) {
                if (item instanceof entity.Tool2 && item.getItemName().equals(weaponName)) {
                    chosenTool = (entity.Tool2) item;
                    break;
                }
            }
            if (chosenTool == null || chosenTool.getOwnCount() <= 0) {
                return "你没有该武器或数量不足，请选择其他武器。";
            }
            weaponBonus = chosenTool.getAttackBonus();
        }

        int playerAttack = player.getBaseAttack() + weaponBonus;
        int monsterAttack = currentMonster.getAttack();
        String result;
        int totalPower = playerAttack + monsterAttack;
        boolean playerWins = random.nextInt(totalPower) < playerAttack;
        if (playerWins) {
            addItemToBackpack(currentMonster.getDropItem().getItemName(), currentMonster.getDropItem().getOwnCount());
            bagUpDate();
            result = "你使用" + weaponName + "攻击" + currentMonster.getName() + "，击败了怪物！获得：" + currentMonster.getDropItem().getItemName() + "。";
        } else {
            int lostHp = Math.max(1, monsterAttack - playerAttack);
            player.setHp(Math.max(0, player.getHp() - lostHp));
            if (player.getHp() <= 0) {
                player.setGameOver(true);
                result = "你使用" + weaponName + "攻击" + currentMonster.getName() + "失败，被怪物反击失去" + lostHp + "点生命，生命值归零，游戏结束！";
            } else {
                result = "你使用" + weaponName + "攻击" + currentMonster.getName() + "失败，被怪物反击失去" + lostHp + "点生命，当前生命值：" + player.getHp() + "。";
            }
        }
        clearCurrentMonster();
        return result;
    }


    /**
     * 1. 沙滩Beach 专属随机事件
     * 怪物：无任何怪物
     * 材料：贝壳、椰子
     * 事件分支：0碎片 / 1本地材料 / 2无怪物 / 3无事件
     */
    public String beachRandomEvent(){
        // 扣减1点行动点（可自定义数值）
        player.setActionPoint(player.getActionPoint() - 1);
        //每次探索 疲惫＋5
        player.setFatigue(player.getFatigue()+5);
        player.setHunger(player.getHunger()+2);
        player.setThirst(player.getThirst()+2);

        int eventType = random.nextInt(10);
        Item2[] backpack = player.getBackpackArr();
        if (eventType == 0) {
            // 获得灯塔碎片
            for(Item2 item : backpack){
                if("灯塔碎片".equals(item.getItemName())){
                    item.setOwnCount(item.getOwnCount() + 1);
                    player.setFragment(player.getFragment() + 1);
                    break;
                }
            }
            bagUpDate();
            return finishEventResult("恭喜！你找到了1块灯塔碎片!");
        } else if (eventType < 5) {
            // 沙滩专属材料
            String[] matList = {"贝壳","椰子"};
            String targetMat = matList[random.nextInt(matList.length)];
            int addNum = random.nextInt(3) + 1;
            for(Item2 item : backpack){
                if(targetMat.equals(item.getItemName())){
                    item.setOwnCount(item.getOwnCount() + addNum);
                    break;
                }
            }
            bagUpDate();
            return finishEventResult("恭喜！" + targetMat + "+" + addNum);
        } else {
            return finishEventResult("无事发生!");
        }
    }

    /**
     * 2. 森林Forest 专属随机事件
     * 怪物：野兔、野猪；材料：木头、藤蔓
     */
    public String forestRandomEvent(){
        // 扣减1点行动点（可自定义数值）
        player.setActionPoint(player.getActionPoint() - 1);
        //每次探索 疲惫＋5
        player.setFatigue(player.getFatigue()+5);
        player.setHunger(player.getHunger()+2);
        player.setThirst(player.getThirst()+2);

        int eventType = random.nextInt(10);
        Item2[] backpack = player.getBackpackArr();
        if (eventType == 0) {
            for(Item2 item : backpack){
                if("灯塔碎片".equals(item.getItemName())){
                    item.setOwnCount(item.getOwnCount() + 1);
                    player.setFragment(player.getFragment() + 1);
                    break;
                }
            }
            bagUpDate();
            return finishEventResult("恭喜！你找到了1块灯塔碎片!");
        } else if (eventType < 4) {
            String[] matList = {"木头","藤蔓"};
            String targetMat = matList[random.nextInt(matList.length)];
            int addNum = random.nextInt(3) + 1;
            for(Item2 item : backpack){
                if(targetMat.equals(item.getItemName())){
                    item.setOwnCount(item.getOwnCount() + addNum);
                    break;
                }
            }
            bagUpDate();
            return finishEventResult("恭喜！" + targetMat + "+" + addNum);
        } else if (eventType < 7) {
            Monster2 monster;
            if(random.nextBoolean()){
                monster = new Hare2();
            }else{
                monster = new Boar2();
            }
            currentMonster = monster;
            return finishEventResult("遭遇" + monster.getName() + "，请选择武器进行战斗。");
        } else {
            return finishEventResult("无事发生!");
        }
    }

    /**
     * 3. 岩石Rocky 专属随机事件
     * 怪物：仅野猪；材料：石头、矿石
     */
    public String rockyRandomEvent(){
        // 扣减1点行动点（可自定义数值）
        player.setActionPoint(player.getActionPoint() - 1);
        //每次探索 疲惫＋5
        player.setFatigue(player.getFatigue()+5);
        player.setHunger(player.getHunger()+2);
        player.setThirst(player.getThirst()+2);

        int eventType = random.nextInt(10);
        Item2[] backpack = player.getBackpackArr();
        if (eventType == 0) {
            for(Item2 item : backpack){
                if("灯塔碎片".equals(item.getItemName())){
                    item.setOwnCount(item.getOwnCount() + 1);
                    player.setFragment(player.getFragment() + 1);
                    break;
                }
            }
            bagUpDate();
            return finishEventResult("恭喜！你找到了1块灯塔碎片!");
        } else if (eventType < 5) {
            String[] matList = {"石头","矿石"};
            String targetMat = matList[random.nextInt(matList.length)];
            int addNum = random.nextInt(3) + 1;
            for(Item2 item : backpack){
                if(targetMat.equals(item.getItemName())){
                    item.setOwnCount(item.getOwnCount() + addNum);
                    break;
                }
            }
            bagUpDate();
            return finishEventResult("恭喜！" + targetMat + "+" + addNum);
        } else if (eventType < 7) {
            currentMonster = new Boar2();
            return finishEventResult("遭遇" + currentMonster.getName() + "，请选择武器进行战斗。");
        } else {
            return finishEventResult("无事发生!");
        }
    }

    /**
     * 4. 海洋Sea 专属随机事件
     * 怪物：仅鱼；材料：贝壳、鱼
     */
    public String seaRandomEvent(){
        // 扣减1点行动点（可自定义数值）
        player.setActionPoint(player.getActionPoint() - 1);
        //每次探索 疲惫＋5
        player.setFatigue(player.getFatigue()+5);
        player.setHunger(player.getHunger()+2);
        player.setThirst(player.getThirst()+2);

        int eventType = random.nextInt(10);
        Item2[] backpack = player.getBackpackArr();
        if (eventType == 0) {
            for(Item2 item : backpack){
                if("灯塔碎片".equals(item.getItemName())){
                    item.setOwnCount(item.getOwnCount() + 1);
                    player.setFragment(player.getFragment() + 1);
                    break;
                }
            }
            bagUpDate();
            return finishEventResult("恭喜！你找到了1块灯塔碎片!");
        } else if (eventType < 5) {
            String[] matList = {"贝壳","鱼"};
            String targetMat = matList[random.nextInt(matList.length)];
            int addNum = random.nextInt(3) + 1;
            for(Item2 item : backpack){
                if(targetMat.equals(item.getItemName())){
                    item.setOwnCount(item.getOwnCount() + addNum);
                    break;
                }
            }
            bagUpDate();
            return finishEventResult("恭喜！" + targetMat + "+" + addNum);
        } else if (eventType < 7) {
            currentMonster = new Fish2();
            return finishEventResult("遭遇" + currentMonster.getName() + "，请选择武器进行战斗。");
        } else {
            return finishEventResult("无事发生!");
        }
    }

    private String finishEventResult(String result) {
        gameEnd();
        return result;
    }

    /**
     * 背包物品数量更新校正：专门处理拾取、消耗后的数量修正
     * 所有拾取、掉落、消耗物品操作后都必须调用本方法刷新背包
     */
    public void bagUpDate() {
        if(gameEnd()){
            return;
        }
        Item2[] backpack = player.getBackpackArr();
        for(Item2 item : backpack){
            // 拾取/消耗后数值可能出现负数，统一校正下限为0
            if(item.getOwnCount() < 0){
                item.setOwnCount(0);
            }
        }
        gameEnd();
    }
    // ai: 返回背包中已有武器的最大攻击加成（若无武器返回0）
    private int getBestWeaponBonus() {
        int best = 0;
        Item2[] backpack = player.getBackpackArr();
        for (Item2 it : backpack) {
            if (it instanceof entity.Tool2) {
                entity.Tool2 t = (entity.Tool2) it;
                if (t.getOwnCount() > 0 && t.getAttackBonus() > best) {
                    best = t.getAttackBonus();
                }
            }
        }
        return best;
    }

    // ai: 根据物品名称增加背包对应物品数量（掉落/拾取使用）
    private void addItemToBackpack(String itemName, int count) {
        Item2[] backpack = player.getBackpackArr();
        for (Item2 it : backpack) {
            if (it.getItemName().equals(itemName)) {
                it.setOwnCount(it.getOwnCount() + count);
                // 若是碎片同时更新玩家fragment计数
                if ("灯塔碎片".equals(itemName)) {
                    player.setFragment(player.getFragment() + count);
                }
                return;
            }
        }
        // ai: 尝试宽松匹配（例如怪物掉落名称为"鱼肉"但背包项为"鱼"）
        if (itemName != null && itemName.endsWith("肉")) {
            String base = itemName.substring(0, itemName.length()-1);
            for (Item2 it : backpack) {
                if (it.getItemName().equals(base)) {
                    it.setOwnCount(it.getOwnCount() + count);
                    return;
                }
            }
        }
        // 若找不到对应条目则不处理（保持原有背包结构不变）
    }
    /**
     * 工作台合成物品预留空方法，和截图结构保持一致
     */
    public void makeItem() {}

    /**
     * 切换下一天逻辑：仅恢复行动点，饥饿、口渴、天数等其他属性完全不变
     * 执行流程：触发当日随机事件 → 仅重置行动点为10 → 刷新背包
     */
    public void nextDay() {
        if(gameEnd()){
            return;
        }
        // 仅恢复行动点，其他生存属性（天数、饥饿、口渴、疲惫）全部不改动
        player.setActionPoint(10);
        // 最后校正背包数据
        bagUpDate();
        gameEnd();
    }

    /**
     * 判断游戏是否结束，返回布尔值
     * 失败条件：血量≤0 / 饥饿≥100 / 口渴≥100 / 疲惫≥100 / 30天内未集齐碎片
     * 胜利条件：碎片≥20块
     */
    public boolean gameEnd() {
        int hp = player.getHp();
        int hunger = player.getHunger();
        int thirst = player.getThirst();
        int fatigue = player.getFatigue();
        int day = player.getDay();
        int frag = player.getFragment();

        boolean isLose = hp <= 0 || hunger >= 100 || thirst >= 100 || fatigue >= 100 || (day >= 30 && frag < MAX_FRAGMENT);
        boolean isWin = frag >= MAX_FRAGMENT;

        player.setGameOver(isLose);
        player.setGameWin(isWin);

        return isLose || isWin;
    }

    // ===================== 存档读档 =====================

    //存档（默认文件名 gameSave.dat）
    public boolean saveGame() {
        return FileUtil2.getInstance().saveGame(player);
    }

    //存档（指定文件名）
    public boolean saveGame(String fileName) {
        return FileUtil2.getInstance().saveGame(player, fileName);
    }

    // 读档（默认文件名 gameSave.dat）
    public boolean loadGame() {
        Player2 loaded = FileUtil2.getInstance().loadGame();
        if (loaded == null) {
            return false;
        }
        copyPlayerData(loaded);
        System.out.println(" 游戏加载完成！");
        return true;
    }

    //读档（指定文件名）
    public boolean loadGame(String fileName) {
        Player2 loaded = FileUtil2.getInstance().loadGame(fileName);
        if (loaded == null) {
            return false;
        }
        copyPlayerData(loaded);
        System.out.println(" 游戏加载完成！");
        return true;
    }

    // 删除存档（默认文件名）
    public boolean deleteSave() {
        return FileUtil2.getInstance().deleteSave();
    }

    //删除存档（指定文件名）
    public boolean deleteSave(String fileName) {
        return FileUtil2.getInstance().deleteSave(fileName);
    }

    //检查存档是否存在（默认文件名）
    public boolean hasSave() {
        return FileUtil2.getInstance().hasSave();
    }

    //检查存档是否存在（指定文件名）
    public boolean hasSave(String fileName) {
        return FileUtil2.getInstance().hasSave(fileName);
    }

    //获取所有存档文件名列表
    public String[] listSaveFiles() {
        return FileUtil2.getInstance().listSaveFiles();
    }


    // ===================== 复制玩家数据（读档用） =====================

    private void copyPlayerData(Player2 loaded) {
        // 基础属性
        player.setHp(loaded.getHp());
        player.setHunger(loaded.getHunger());
        player.setThirst(loaded.getThirst());
        player.setFatigue(loaded.getFatigue());
        player.setActionPoint(loaded.getActionPoint());
        player.setDay(loaded.getDay());
        player.setFragment(loaded.getFragment());
        player.setBaseAttack(loaded.getBaseAttack());
        player.setSpace(loaded.getSpace());
        player.setGameOver(loaded.isGameOver());
        player.setGameWin(loaded.isGameWin());

        // 背包数据深拷贝
        Item2[] loadedBag = loaded.getBackpackArr();
        Item2[] currentBag = player.getBackpackArr();

        for (int i = 0; i < loadedBag.length && i < currentBag.length; i++) {
            Item2 loadedItem = loadedBag[i];
            Item2 currentItem = currentBag[i];

            if (loadedItem != null && currentItem != null) {
                // 复制数量
                currentItem.setOwnCount(loadedItem.getOwnCount());
            }
        }
    }
}