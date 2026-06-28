package entity;

public class Rabbit2 extends Monster2{
    public Rabbit2(){
        super("兔",5,new Item2("兔肉",1));
    }

    public Rabbit2(String name,int attack,Item2 dropItem){
        super(name, attack, dropItem);
    }
}
