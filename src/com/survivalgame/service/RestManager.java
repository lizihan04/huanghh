package com.survivalgame.service;

import entity.*;
import java.util.*;

public class RestManager {

    private static final RestManager instance = new RestManager();
    private RestManager() {}
    public static RestManager getInstance() {
        return instance;
    }

    private Player player;
    private Map<String, Terrain> terrains;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTerrains(Map<String, Terrain> terrains) {
        this.terrains = terrains;
    }

    private Terrain getTerrain(String sceneType) {
        return terrains.get(sceneType);
    }

    public void rest() {
        if (player.getActionPoint() <= 0) {
            System.out.println("行动力不足，自动进入下一天");
            return;
        }

        player.rest();
        Terrain terrain = getTerrain(player.getCurrentArea());
        String effect = terrain.getRestEffect(player);
        System.out.println(effect);
        player.checkGameOver();
    }
}