package com.survivalgame.service;

import entity.*;
import java.util.*;

public class ExploreManager {

    private static final ExploreManager instance = new ExploreManager();

    private ExploreManager() {
    }

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

    public Monster explore() {
        if (player.getActionPoint() <= 0) {
            System.out.println("行动力不足，自动进入下一天");
            return null;
        }

        player.doAction(null);
        Terrain terrain = getTerrain(player.getCurrentArea());
        double r = random.nextDouble();

        if (r < 0.05) {
            player.addItem("灯塔碎片", 1);
            System.out.println("✨ 在【" + player.getCurrentArea() + "】探索，发现了一块灯塔碎片！");
            return null;
        } else if (r < 0.45) {
            Item resource = terrain.createResource();
            if (resource != null) {
                player.addItem(resource.getItemName(), resource.getOwnCount());
                System.out.println("在【" + player.getCurrentArea() + "】探索，发现 " + resource.getItemName() + " x" + resource.getOwnCount());
            } else {
                System.out.println("探索一番，什么也没发现");
            }
            return null;
        } else if (r < 0.85) {
            Monster monster = terrain.createMonster();
            if (monster != null) {
                System.out.println("在【" + player.getCurrentArea() + "】探索，遭遇 " + monster.getName() + "！");
                return monster;
            } else {
                System.out.println("探索一番，没有发现怪物");
                return null;
            }
        } else {
            System.out.println("在【" + player.getCurrentArea() + "】探索了一番，什么也没有发生");
            return null;
        }
    }
}