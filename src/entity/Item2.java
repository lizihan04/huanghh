package entity;

import java.io.Serializable;

public class Item2 {
    private String itemName;       // 物品名称

    // 动态：持有数量（构造传入）
    private int ownCount;

    //构造方法
    public Item2() {
    }

    public Item2(String itemName, int ownCount) {
        this.itemName = itemName;
        this.ownCount = ownCount;
    }

    //getter and setter
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getOwnCount() {
        return ownCount;
    }

    public void setOwnCount(int ownCount) {
        this.ownCount = ownCount;
    }
}