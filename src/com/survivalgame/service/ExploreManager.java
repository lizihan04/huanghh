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

        if (r < 0.4) {
            Item resource = terrain.createResource();
            if (resource != null) {
                player.addItem(resource);
                System.out.println("在【" + player.getCurrentArea() + "】探索，发现 " + resource.getName() + " x" + resource.getCount());
            } else {
                System.out.println("探索一番，什么也没发现");
            }
        } else if (r < 0.8) {
            Monster monster = terrain.createMonster();
            if (monster != null) {
                System.out.println("在【" + player.getCurrentArea() + "】探索，遭遇 " + monster.getName() + "！");
            } else {
                System.out.println("探索一番，没有发现怪物");
            }
        } else {
            System.out.println("在【" + player.getCurrentArea() + "】探索了一番，什么也没有发生");
        }
        player.checkGameOver();
    }
}