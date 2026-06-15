package entity;

public class Crab extends Monster{
    public Crab() {
        setName("螃蟹");
        setLife(50);
        setAttack(10);
        setImgPath("img/crab.png");
    }

    //重写攻击方法
    @Override
    public void attack(Player player) {
        int atk = getAttack();
        player.setHp(player.getHp() - atk);
        System.out.println("螃蟹挥舞钳子攻击，对你造成 " + atk + " 点伤害！");
    }

    @Override
    protected Item getFixedDrop() {
        // 螃蟹掉落蟹肉
        return new Food("蟹肉", "食物", "恢复饥饿值", "img/crab_meat.png", 1, "生命值", 20);
    }
}
