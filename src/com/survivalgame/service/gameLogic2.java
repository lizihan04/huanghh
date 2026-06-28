package com.survivalgame.service;

import entity.Player2;

public class gameLogic2 {
    Player2 player = Player2.getInstance();

    //触发事件===>随机事件
    //1、获得碎片
    //根据不同的地图，有不同的材料和怪物
    //2、获得材料
    //3、遇到怪物（沙滩为椰子树）
    //4、什么都没有发生
    public void randomEvent(){}

    //背包物品数量的更新
    public void bagUpDate(){}

    //工作台制作物品（根据配方类recipe）
    public void makeItem(){}

    //下一天
    public void nextDay(){}

    //判断游戏进程（布尔值的赋值）
    public void gameEnd(){}
}
