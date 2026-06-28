package entity;

import java.io.Serializable;

public class Hare2 extends Monster2{
    //构造方法
    public Hare2() {
        super("野兔", 5, new Item2("兔肉", 1));
    }

    public Hare2(String name, int attack, Item2 dropItem) {
        super(name, attack, dropItem);
    }
}
