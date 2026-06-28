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
        // 使用Item食物专属构造器，参数规则：
        // Item(名称, itemType固定food, 描述, 图片路径, 恢复类型hp/hunger/fatigue/thirst, 恢复数值, 持有数量)
        return new Item("鲨鱼肉", "food", "恢复生命", "item_fish.png", "hp", 35, 1);
    }
}