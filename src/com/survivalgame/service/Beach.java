package com.survivalgame.service;

import entity.*;
import java.util.Random;

public class Beach implements Terrain {

    private static final Beach instance = new Beach();
    private Beach() {}
    public static Beach getInstance() {
        return instance;
    }

    private final Random random = new Random();

    @Override
    public String getSceneType() {
        return "沙滩";
    }

    @Override
    public Item createResource() {
        if (random.nextBoolean()) {
            return new Item("树枝", "material", "合成材料", "images/img_item/material/item_stick.png", 1 + random.nextInt(2));
        } else {
            // 椰子：食物构造器 Item(name, type, desc, img, recoverType, recoverValue, ownCount)
            return new Item("椰子", "food", "解渴", "images/img_item/food/item_coconut.png", "thirst", 20, 1);
        }
    }

    @Override
    public Monster createMonster() {
        return new Crab();
    }

    @Override
    public String getRestEffect(Player player) {
        int oldFatigue = player.getFatigue();
        int newFatigue = Math.max(0, oldFatigue - 20);
        player.setFatigue(newFatigue);
        int oldHp = player.getHp();
        int newHp = Math.min(100, oldHp + 10);
        player.setHp(newHp);
        return "在沙滩休息，疲惫-20，血量+10";
    }
}