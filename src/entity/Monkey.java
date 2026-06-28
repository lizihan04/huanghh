package entity;
import java.util.Random;

public class Monkey extends Monster {
    private final Random random = new Random();
    // 偷窃失手概率30%
    private static final int STEAL_FAIL_RATE = 3;

    public Monkey() {
        super.setName("猴子");
        super.setLife(100);
        super.setAttack(20);
        super.setImgPath("monster_monkey.png");
    }

    @Override
    public void attack(Player player) {
        System.out.println("\n🐒 野猴窜了出来，想要偷窃你的物品！");
        int stealState = trySteal(player);

        // 偷窃失败则攻击玩家
        if (stealState != 1) {
            int atk = this.getAttack();
            System.out.println("猴子发怒，给了你一个大比兜！");
            player.setHp(player.getHp() - atk);
            System.out.println("你受到 " + atk + " 点伤害！");
        }
    }

    /**
     * 偷窃逻辑
     * @return 1成功 2无物品 3失手
     */
    private int trySteal(Player player) {
        Item targetItem = player.getRandomStealableItem();

        if (targetItem == null) {
            System.out.println("猴子翻找一番，发现你身上没有能偷的东西！");
            return 2;
        }

        int rand = random.nextInt(10);
        if (rand < STEAL_FAIL_RATE) {
            System.out.println("眼看就要得手，猴子却不慎失手，东西掉回了你包里！");
            return 3;
        }

        // 修正旧方法 reduceCount1 → reduceCount / getName → getItemName
        targetItem.reduceCount(1);
        System.out.println("猴子得手，偷走了 1 个【" + targetItem.getItemName() + "】！");
        return 1;
    }

    // 怪物死亡掉落，删除new Food，使用Item食物构造
    @Override
    protected Item getFixedDrop() {
        // 食物构造参数：name,type,desc,img,recoverType,val,count
        return new Item("野果", "food", "恢复少量饥饿值", "img/fruit.png", "hunger", 10, 1);
    }
}