package entity;

public class TigerShark extends Monster {

    public TigerShark() {
        setName("虎鲨");
        setLife(120);
        setAttack(18);
        setImgPath("img_monster/monster_shark.png");
    }

    @Override
    public void attack(Player player) {
        int atk = getAttack();
        player.setHp(player.getHp() - atk);
        System.out.println("虎鲨咬了你一口，对你造成 " + atk + " 点伤害！");
    }

    @Override
    protected Item getFixedDrop() {
        // 虎鲨掉落鱼肉
        return new Food("鲨鱼肉", "食物", "恢复生命", "item_fish.png", 1, "生命值", 35);
    }
}