package entity;

import java.io.Serializable;

public class Fish2 extends Monster2{
    //构造方法
    public Fish2() {
        super("鱼", 5, new Item2("鱼肉", 1));
    }

    public Fish2(String name, int attack, Item2 dropItem) {
        super(name, attack, dropItem);
    }


}
