package entity;

public class Hare extends Monster {
    public Hare() {
        setName("野兔");
        setLife(35);
        setAttack(4);
        setImgPath("img/hare.png");
    }

    @Override
    public void attack(Player player) {
        int atk = getAttack();
        player.takeDamage(atk);
        System.out.println("野兔慌乱蹬腿踢你，造成 " + atk + " 点伤害！");
    }

    @Override
    protected Item getFixedDrop() {
        // 兔肉：恢复血量 hp
        return new Item("兔肉", "food", "恢复生命值", "img/hare_meat.png", "hp", 12, 1);
    }
}