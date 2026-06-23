package entity;

public class BlueSheep extends Monster{
        public BlueSheep() {
            setName("岩羊");
            setLife(80);
            setAttack(7);
            setImgPath("img/sheep.png");
        }

        @Override
        public void attack(Player player) {
            int atk = getAttack();
            player.takeDamage(atk);
            System.out.println("岩羊用角狠狠顶了你一下，对你造成 " + atk + " 点伤害！");
        }

        @Override
        protected Item getFixedDrop() {
            // 岩羊掉落羊肉
            return new Food("羊肉", "食物", "恢复生命", "img/mutton.png", 1, "生命值", 25);
        }

}
