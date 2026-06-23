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
            int atk1 = getAttack();
            int atk2 = 20;
            int randomNum =(int)(Math.random()*2);
            if(randomNum==0){
                player.setHp(player.getHp() - atk1);
                System.out.println("岩羊用角狠狠顶了你一下，对你造成 " + atk1 + " 点伤害！");
            }else if(randomNum==1){
                player.setHp(player.getHp() - atk2);
                System.out.println("岩羊用脚狠狠踢了你一下，对你造成 " + atk2 + " 点伤害！");
            }

        }

        @Override
        protected Item getFixedDrop() {
            // 岩羊掉落羊肉
            return new Food("羊肉", "食物", "恢复生命", "img/mutton.png", 1, "生命值", 25);
        }

}
