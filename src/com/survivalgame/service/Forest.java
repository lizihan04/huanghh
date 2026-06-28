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
        return new Item("藤蔓", "material", "合成材料", "images/img_item/material/item_vine.png", count);
    }

    @Override
    public Monster createMonster() {
        return new Monkey();
    }

    // 删除 getRestEffect 方法
}