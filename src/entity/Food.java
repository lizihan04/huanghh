package entity;

public class Food extends Item{
    //食物独有的属性：恢复类型，单次恢复数值
    private String recoverType;
    private int recoverValue;

    //构造方法
    public Food(String name, String type, String effect, String imgPath, int count, String recoverType, int recoverValue) {
        super(name, type, effect, imgPath, count);
        this.recoverType = recoverType;
        this.recoverValue = recoverValue;
    }

    //getter和setter方法
    public String getRecoverType() {
        return recoverType;
    }

    public void setRecoverType(String recoverType) {
        this.recoverType = recoverType;
    }

    public int getRecoverValue() {
        return recoverValue;
    }

    public void setRecoverValue(int recoverValue) {
        this.recoverValue = recoverValue;
    }

    //重写使用方法
    @Override
    public void use(Player player) {
        //判断物品数量，小于零则无法使用
        if (this.getCount() <= 0) {
            System.out.println(this.getName()+"数量不足!");
            return;
        }
        //判断玩家是否已经死亡，死亡则无法使用
        if (player.getHp() <= 0) {
            System.out.println("玩家已经死亡，无法使用物品");
            return;
        }
        //判断恢复类型,恢复对应数值
        if ("hunger".equals(recoverType)) {
            player.setHunger(player.getHunger() + recoverValue);
            System.out.println("使用" + super.getName() + "，饥饿值恢复" + recoverValue);
        }else if ("口渴值".equals(recoverType)) {
            player.setThirst(player.getThirst() + recoverValue);
        }else if ("疲惫值".equals(recoverType)) {
            player.setFatigue(player.getFatigue() + recoverValue);
        }
        super.reduceCount2(1);
        this.setCount(this.getCount() - 1);
    }
}





