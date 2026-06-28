package com.survivalgame.service;

import entity.Boar2;
import entity.Fish2;
import entity.Hare2;
import entity.Item2;
import entity.Monster2;
import entity.Player2;
import java.util.Random;

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

    /**
     * 随机事件：4类分支
     * 0=获得碎片 1=获得随机材料 2=遭遇怪物扣血 3=无事件
     */
    public void randomEvent() {
        if(gameEnd()){
            return;
        }
        int eventType = random.nextInt(4);
        Item2[] backpack = player.getBackpackArr();
        switch (eventType){
            case 0:
                // 事件1：拾取灯塔碎片
                for(Item2 item : backpack){
                    if("灯塔碎片".equals(item.getItemName())){
                        item.setOwnCount(item.getOwnCount() + 1);
                        player.setFragment(player.getFragment() + 1);
                        break;
                    }
                }
                // 拾取物品后更新背包数量
                bagUpDate();
                break;
            case 1:
                // 事件2：拾取随机基础材料
                String[] matList = {"矿石","贝壳","石头","藤蔓","木头"};
                String targetMat = matList[random.nextInt(matList.length)];
                int addNum = random.nextInt(3) + 1;
                for(Item2 item : backpack){
                    if(targetMat.equals(item.getItemName())){
                        item.setOwnCount(item.getOwnCount() + addNum);
                        break;
                    }
                }
                // 拾取物品后更新背包数量
                bagUpDate();
                break;
            case 2:
                // 事件3：随机生成怪物，按怪物自身攻击值扣玩家血量
                Monster2 monster;
                int monsterRand = random.nextInt(3);
                if(monsterRand == 0){
                    monster = new Hare2(); // 野兔伤害5
                }else if(monsterRand == 1){
                    monster = new Fish2(); // 鱼伤害5
                }else{
                    monster = new Boar2(); // 野猪伤害10
                }
                int hurt = monster.getAttack();
                int nowHp = player.getHp();
                player.setHp(Math.max(0, nowHp - hurt));
                break;
            case 3:
                // 事件4：无任何事件
                break;
        }
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
        // 第一步：执行当日随机事件（包含物品拾取，内部自动调用bagUpDate）
        randomEvent();
        // 仅恢复行动点，其他生存属性（天数、饥饿、口渴、疲惫）全部不改动
        player.setActionPoint(10);
        // 最后校正背包数据
        bagUpDate();
    }

    /**
     * 判断游戏是否结束，返回布尔值
     * 失败条件：血量≤0 / 饥饿≥100 / 口渴≥100 / 疲惫≥100 / 天数>30
     * 胜利条件：碎片≥20块
     */
    public boolean gameEnd() {
        int hp = player.getHp();
        int hunger = player.getHunger();
        int thirst = player.getThirst();
        int fatigue = player.getFatigue();
        int day = player.getDay();
        int frag = player.getFragment();

        boolean isLose = hp <= 0 || hunger >= 100 || thirst >= 100 || fatigue >= 100 || day > 30;
        boolean isWin = frag >= MAX_FRAGMENT;
        return isLose || isWin;
    }
}