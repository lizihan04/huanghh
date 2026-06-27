package entity;

public class Boar extends Monster {
    public Boar() {
        // 属性
        setName("野猪");
        setLife(95);
        setAttack(12);
        setImgPath("monster_boar.png");
    }

    // 实现攻击抽象方法
    @Override
    public void attack(Player player) {
        int atk = getAttack();
        player.setHp(player.getHp() - atk);
        System.out.println("野猪冲撞，对你造成 " + atk + " 点伤害！");
    }

    // 掉落野猪肉
    @Override
    protected Item getFixedDrop() {
        return new Food("野猪肉", "食物", "恢复生命值", "item_pork.png", 1, "生命值", 28);
    }
}