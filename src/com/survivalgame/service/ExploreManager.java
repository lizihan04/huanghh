package com.survivalgame.service;

import entity.*;
import java.util.*;

public class ExploreManager {

    private static final ExploreManager instance = new ExploreManager();
    private ExploreManager() {}
    public static ExploreManager getInstance() {
        return instance;
    }

    private Player player;
    private Random random;
    private Map<String, Terrain> terrains;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public void setTerrains(Map<String, Terrain> terrains) {
        this.terrains = terrains;
    }

    private Terrain getTerrain(String sceneType) {
        return terrains.get(sceneType);
    }

    public void explore() {
        if (player.getActionPoint() <= 0) {
            System.out.println("行动力不足，自动进入下一天");
            return;
        }

        player.doAction(null);
        Terrain terrain = getTerrain(player.getCurrentArea());
        double r = random.nextDouble();

        // 5%概率获得灯塔碎片
        if (r < 0.05) {
            // 创建灯塔碎片
            Clip fragment = new Clip("灯塔碎片", "fragment", "集齐20块可通关",
                    "images/img_item/tool/item_tower.png", 1, 1);
            player.addItem(fragment);
            System.out.println("✨ 在【" + player.getCurrentArea() + "】探索，发现了一块灯塔碎片！");
        } else if (r < 0.45) {
            // 40%概率获得物资
            Item resource = terrain.createResource();
            if (resource != null) {
                player.addItem(resource);
                System.out.println("在【" + player.getCurrentArea() + "】探索，发现 " + resource.getName() + " x" + resource.getCount());
            } else {
                System.out.println("探索一番，什么也没发现");
            }
        } else if (r < 0.85) {
            // 40%概率遭遇怪物
            Monster monster = terrain.createMonster();
            if (monster != null) {
                System.out.println("在【" + player.getCurrentArea() + "】探索，遭遇 " + monster.getName() + "！");
                // 战斗由 GameService 处理
            } else {
                System.out.println("探索一番，没有发现怪物");
            }
        } else {
            // 15%概率无事发生（5%碎片 + 40%物资 + 40%怪物 = 85%，剩下15%无事）
            System.out.println("在【" + player.getCurrentArea() + "】探索了一番，什么也没有发生");
        }
        player.checkGameOver();
    }
}