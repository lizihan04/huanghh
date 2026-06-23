package entity;

import java.util.Random;

public class Monkey extends Monster {
    private final Random random = new Random();
    // 设定偷窃失手概率：30% 几率偷东西失败
    private static final int STEAL_FAIL_RATE = 3;

    public Monkey() {
        super.setName("猴子");
        super.setLife(100);
        super.setAttack(20);
        super.setImgPath("img/monkey.png");
    }

    @Override
    public void attack(Player player) {
        System.out.println("\n🐒 野猴窜了出来，想要偷窃你的物品！");
        // 执行偷窃，返回偷窃结果状态
        int stealState = trySteal(player);

        // 两种偷窃失败，都触发攻击
        if (stealState != 1) {
            int atk = this.getAttack();
            System.out.println("猴子发怒，给了你一个大比兜！");
            player.setHp(player.getHp() - atk);
            System.out.println("你受到 " + atk + " 点伤害！");
        }
    }

    /**
     * 偷窃逻辑，返回状态码区分结果
     * @return 1=偷窃成功  2=玩家无物品可偷  3=猴子自身偷窃失手
     */
    private int trySteal(Player player) {
        Item targetItem = player.getRandomStealableItem();

        // 状态2：玩家没有任何可偷物品
        if (targetItem == null) {
            System.out.println("猴子翻找一番，发现你身上没有能偷的东西！");
            return 2;
        }

        // 判定猴子自身是否失手
        int rand = random.nextInt(10);
        if (rand < STEAL_FAIL_RATE) {
            // 状态3：有物品，但猴子偷取失败
            System.out.println("眼看就要得手，猴子却不慎失手，东西掉回了你包里！");
            return 3;
        }

        // 状态1：偷窃成功
        targetItem.reduceCount1(1);
        System.out.println("猴子得手，偷走了 1 个【" + targetItem.getName() + "】！");
        return 1;
    }

    // 重写抽象方法：定义猴子死亡固定掉落物
    @Override
    protected Item getFixedDrop() {
        return new Food("野果", "food", "恢复少量饥饿值", "img/fruit.png", 1, "hunger", 10);
    }
}