package com.survivalgame.service;

import entity.Player;

public class DayManager {

    private static final DayManager instance = new DayManager();
    private DayManager() {}
    public static DayManager getInstance() {
        return instance;
    }

    private Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void nextDay() {
        player.nextDay();
        System.out.println("===== 第 " + player.getDay() + " 天 =====");
        System.out.println("行动点已重置为10");
    }
}