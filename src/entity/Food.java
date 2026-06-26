package entity;

public class Food extends Item implements java.io.Serializable {
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
        //判断物品数量
        if (this.getCount() <= 0) {
            System.out.println(this.getName() + "数量不足!");
            return;
        }
        //判断玩家是否已经死亡
        if (player.getHp() <= 0) {
            System.out.println("玩家已经死亡，无法使用物品");
            return;
        }

        //判断恢复类型,恢复对应数值（hunger饥饿 / thirst口渴 / fatigue疲惫 / hp血量）
        switch (recoverType) {
            case "hunger":
                int newHunger = player.getHunger() + recoverValue;
                player.setHunger(Math.min(100, newHunger));
                System.out.println("使用 " + super.getName() + "，饥饿值恢复 " + recoverValue);
                break;
            case "thirst":
                int newThirst = player.getThirst() + recoverValue;
                player.setThirst(Math.min(100, newThirst));
                System.out.println("使用 " + super.getName() + "，口渴值恢复 " + recoverValue);
                break;
            case "fatigue":
                int newFatigue = player.getFatigue() + recoverValue;
                player.setFatigue(Math.min(100, newFatigue));
                System.out.println("使用 " + super.getName() + "，疲惫值恢复 " + recoverValue);
                break;
            case "hp":
                int newHp = player.getHp() + recoverValue;
                player.setHp(Math.min(100, newHp));
                System.out.println("使用 " + super.getName() + "，血量恢复 " + recoverValue);
                break;
            default:
                System.out.println("该物品无任何效果");
                return;
        }
        super.reduceCount2(1);
    }
}