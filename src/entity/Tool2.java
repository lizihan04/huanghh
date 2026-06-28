package entity;

import java.io.Serializable;

public class Tool2 extends Item2 {
    // 工具专属（无采集加成）
    private int attackBonus;// 攻击加成

    //构造方法
    public Tool2() {
    }

    public Tool2(String itemName, int ownCount, int attackBonus) {
        super(itemName, ownCount);
        this.attackBonus = attackBonus;
    }

    //getter and setter
    public int getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(int attackBonus) {
        this.attackBonus = attackBonus;
    }
}

