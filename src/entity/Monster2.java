package entity;

public abstract class Monster2 {
    //怪物属性（名字、伤害、掉落物品）
    private String name;
    private int attack;
    private Item dropItem;

    //构造方法
    public Monster2() {
    }

    public Monster2(String name, int attack, Item dropItem) {
        this.name = name;
        this.attack = attack;
        this.dropItem = dropItem;
    }

    //getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public Item getDropItem() {
        return dropItem;
    }

    public void setDropItem(Item dropItem) {
        this.dropItem = dropItem;
    }
}
