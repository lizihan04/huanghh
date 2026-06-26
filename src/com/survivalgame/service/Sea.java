package com.survivalgame.service;

import entity.*;
import java.util.Random;

public class Sea implements Terrain {

    private static final Sea instance = new Sea();
    private Sea() {}
    public static Sea getInstance() {
        return instance;
    }

    private final Random random = new Random();

    @Override
    public String getSceneType() {
        return "海边";
    }

    @Override
    public Item createResource() {
        int count = 1 + random.nextInt(2);
        return new Tool("矿石", "material", "合成材料", "images/img_item/material/item_shell.png", count, 0, 0, 0);
    }

    @Override
    public Monster createMonster() {
        return new TigerShark();
    }

    @Override
    public String getRestEffect(Player player) {
        int oldFatigue = player.getFatigue();
        int newFatigue = Math.max(0, oldFatigue - 5);
        player.setFatigue(newFatigue);
        return "在海边休息，疲惫-5（海风太大，难以入睡）";
    }
}