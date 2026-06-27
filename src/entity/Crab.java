package entity;

public class Crab extends Monster{
    public Crab() {
        setName("螃蟹");
        setLife(50);
        setAttack(10);
        setImgPath("monster_crab.png");
    }


    //重写攻击方法
    @Override
    public void attack(Player player) {
        int atk = getAttack();
        player.setHp(player.getHp() - atk);
        System.out.println("螃蟹夹了你一下，对你造成 " + atk + " 点微弱伤害，然后迅速溜走！");
    }

    @Override
    protected Item getFixedDrop() {
        // 螃蟹掉落蟹肉
        return new Food("蟹肉", "食物", "恢复饥饿值", "item_crabrou.png", 1, "生命值", 20);
    }
}
