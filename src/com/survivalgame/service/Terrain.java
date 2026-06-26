package com.survivalgame.service;

import entity.Item;
import entity.Monster;
import entity.Player;

public interface Terrain {
    //获取地形
    String getSceneType();
    //生成该地区特色物资
    Item createResource();
    //该地区特有怪物
    Monster createMonster();
    //该地区休息效果
    String getRestEffect(Player player);
}