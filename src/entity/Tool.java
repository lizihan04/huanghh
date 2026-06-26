package entity;

public class Tool extends Item implements java.io.Serializable {
    //工具独有的属性：攻击加成，采集加成（，防御加成），耐久度
    private int attackBonus;
    private int collectBonus;
    //private int defenseBonus;（要加吗？）
    private int durability;

    public Tool(String name, String type, String effect, String imgPath, int count, int attackBonus, int collectBonus, int durability) {
        super(name, type, effect, imgPath, count);
        this.attackBonus = attackBonus;
        this.collectBonus = collectBonus;
        this.durability = durability;
    }

    //getter和setter方法
    public int getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(int attackBonus) {
        this.attackBonus = attackBonus < 0 ? 0 : attackBonus;
    }

    public int getCollectBonus() {
        return collectBonus;
    }

    public void setCollectBonus(int collectBonus) {
        this.collectBonus = collectBonus < 0 ? 0 : collectBonus;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability < 0 ? 0 : durability;
    }

    //重写使用方法
    @Override
    public void use(Player player) {
        //检验工具是否可用（数量，耐久度）
        if (this.getCount() <= 0) {
            System.out.println(this.getName() + "数量不足，无法使用！");
            return;
        }
        if (this.durability <= 0) {
            System.out.println(this.getName() + "耐久度已耗尽，无法使用！");
            return;
        }

        //工具生效
        System.out.println("使用【" + this.getName() + "】，攻击+" + this.attackBonus + "，采集加成+" + this.collectBonus);

        //消耗耐久度
        this.durability--;
        super.reduceCount2(1);

        //如果工具的耐久度耗尽，则工具数量-1
        if (this.durability == 0) {
            System.out.println(this.getName() + "耐久耗尽，道具损毁！");
            //当工具数量大于0时数量-1
            if (this.getCount() > 0) {
                this.setCount(this.getCount() - 1);
            }
        }
    }

}
