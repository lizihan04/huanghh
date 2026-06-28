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
        return new Item("矿石", "material", "合成材料", "images/img_item/material/item_ore.png", count);
    }

    @Override
    public Monster createMonster() {
        return new TigerShark();
    }
}