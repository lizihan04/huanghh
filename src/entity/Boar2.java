package entity;

import java.io.Serializable;

public class Boar2 extends Monster2{
    //构造方法
    public Boar2() {
        super("野猪", 10, new Item2("猪肉", 1));
    }

    public Boar2(String name, int attack, Item2 dropItem) {
        super(name, attack, dropItem);
    }
}
