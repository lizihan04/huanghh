package com.survivalgame.service;

import entity.*;
import java.util.Random;

public class Forest implements Terrain {

    private static final Forest instance = new Forest();
    private Forest() {}
    public static Forest getInstance() {
        return instance;
    }

    private final Random random = new Random();

    @Override
    public String getSceneType() {
        return "树林";
    }

    @Override
    public Item createResource() {
        int count = 1 + random.nextInt(3);
        return new Tool("藤蔓", "material", "合成材料", "images/img_item/material/item_vine.png", count, 0, 0, 0);
    }

    @Override
    public Monster createMonster() {
        return new Monkey();
    }

    @Override
    public String getRestEffect(Player player) {
        int oldFatigue = player.getFatigue();
        int newFatigue = Math.max(0, oldFatigue - 10);
        player.setFatigue(newFatigue);
        return "在树林休息，疲惫-10";
    }
}