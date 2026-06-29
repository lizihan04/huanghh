package com.survivalgame.service;

import entity.Item2;
import entity.Player2;

public class Recipe2 {
    Player2 player = Player2.getInstance();
    Item2[] backpackArr = player.getBackpackArr();

    //制作木棒
    public void woodStick(){
        if(backpackArr[7].getOwnCount()>=1 && backpackArr[8].getOwnCount()>=2){
            backpackArr[7].setOwnCount(backpackArr[7].getOwnCount()-1);
            backpackArr[8].setOwnCount(backpackArr[8].getOwnCount()-2);
            // ai: 修复合成后目标栏位更新，原代码误用了下标9
            backpackArr[13].setOwnCount(backpackArr[13].getOwnCount()+1);
        }else{
            System.out.println("材料不足");
        }
    }

    //制作贝刃
    public void beiRen(){
        if(backpackArr[13].getOwnCount()>=1&&backpackArr[5].getOwnCount()>=3){
            backpackArr[13].setOwnCount(backpackArr[13].getOwnCount()-1);
            backpackArr[5].setOwnCount(backpackArr[5].getOwnCount()-3);
            backpackArr[11].setOwnCount(backpackArr[11].getOwnCount()+1);
        }else{
            System.out.println("材料不足");
        }
    }

    //制作石剑
    public void stoneSword(){
        if(backpackArr[13].getOwnCount()>=1&&backpackArr[4].getOwnCount()>=2&&backpackArr[6].getOwnCount()>=1){
            backpackArr[13].setOwnCount(backpackArr[13].getOwnCount()-1);
            backpackArr[4].setOwnCount(backpackArr[4].getOwnCount()-2);
            backpackArr[6].setOwnCount(backpackArr[6].getOwnCount()-1);
            backpackArr[14].setOwnCount(backpackArr[14].getOwnCount()+1);
        }else{
            System.out.println("材料不足");
        }
    }

    //制作斧头
    public void axe(){
        if(backpackArr[13].getOwnCount()>=1&&backpackArr[4].getOwnCount()>=2&&backpackArr[6].getOwnCount()>=1){
            backpackArr[13].setOwnCount(backpackArr[13].getOwnCount()-1);
            backpackArr[4].setOwnCount(backpackArr[4].getOwnCount()-2);
            backpackArr[6].setOwnCount(backpackArr[6].getOwnCount()-1);
            backpackArr[10].setOwnCount(backpackArr[10].getOwnCount()+1);
        }else{
            System.out.println("材料不足");
        }
    }

    //制作锤子
    public void hammer(){
        if(backpackArr[13].getOwnCount()>=1&&backpackArr[4].getOwnCount()>=2&&backpackArr[6].getOwnCount()>=1){
            backpackArr[13].setOwnCount(backpackArr[13].getOwnCount()-1);
            backpackArr[4].setOwnCount(backpackArr[4].getOwnCount()-2);
            backpackArr[6].setOwnCount(backpackArr[6].getOwnCount()-1);
            backpackArr[12].setOwnCount(backpackArr[12].getOwnCount()+1);
        }else{
            System.out.println("材料不足");
        }
    }

    //制作碎片
    public void fragment(){
        if(backpackArr[4].getOwnCount()>=4){
            backpackArr[4].setOwnCount(backpackArr[4].getOwnCount()-4);
            backpackArr[9].setOwnCount(backpackArr[9].getOwnCount()+1);
        }else{
            System.out.println("材料不足");
        }
    }


}
