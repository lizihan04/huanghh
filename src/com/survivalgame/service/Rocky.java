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
        return new Item("石头", "material", "合成材料", "images/img_item/material/item_stone.png", count);
    }

    @Override
    public Monster createMonster() {
        return new BlueSheep();
    }
}