package entity;

public class Crab extends Monster{
    public Crab() {
        setName("螃蟹");
        setLife(50);
        setAttack(10);
        setImgPath("monster_crab.png");
    }

    @Override
    public void attack(Player player) {
        int atk = getAttack();
        player.setHp(player.getHp() - atk);
        System.out.println("螃蟹夹了你一下，对你造成 " + atk + " 点微弱伤害，然后迅速溜走！");
    }

    @Override
    protected Item getFixedDrop() {
        // 蟹肉：描述恢复饥饿，对应 recoverType=hunger
        return new Item("蟹肉", "food", "恢复饥饿值", "item_crabrou.png", "hunger", 20, 1);
    }
}