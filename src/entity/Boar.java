package entity;

public class Boar extends Monster {
    public Boar() {
        setName("野猪");
        setLife(95);
        setAttack(12);
        setImgPath("monster_boar.png");
    }

    @Override
    public void attack(Player player) {
        int atk = getAttack();
        player.setHp(player.getHp() - atk);
        System.out.println("野猪冲撞，对你造成 " + atk + " 点伤害！");
    }

    @Override
    protected Item getFixedDrop() {
        // 野猪肉：恢复血量 hp
        return new Item("野猪肉", "food", "恢复生命值", "item_pork.png", "hp", 28, 1);
    }
}