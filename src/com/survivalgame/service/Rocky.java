package com.survivalgame.service;

import entity.*;
import java.util.Random;

public class Rocky implements Terrain {

    private static final Rocky instance = new Rocky();
    private Rocky() {}
    public static Rocky getInstance() {
        return instance;
    }

    private final Random random = new Random();

    @Override
    public String getSceneType() {
        return "岩石区";
    }

    @Override
    public Item createResource() {
        int count = 1 + random.nextInt(3);
        return new Tool("石头", "material", "合成材料", "images/img_item/material/item_stone.png", count, 0, 0, 0);
    }

    @Override
    public Monster createMonster() {
        return new BlueSheep();
    }

    @Override
    public String getRestEffect(Player player) {
        int oldFatigue = player.getFatigue();
        int newFatigue = Math.max(0, oldFatigue - 8);
        player.setFatigue(newFatigue);
        return "在岩石区休息，疲惫-8（夜晚太寒冷了）";
    }
}