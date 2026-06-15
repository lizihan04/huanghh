package entity;

public abstract class Monster {
    private String name;//名字
    private int hp;//生命
    private int attack;//攻击力
    private String desc;//介绍
    private String imgPath;//图片路径

    /**
     * 返回当前怪物统一掉落模板
     * getDropTemplate()：抽象方法，由怪物子类（野猪 / 野兔）实现
     * 所有野猪 → 统一返回「野猪肉」模板
     * 所有野兔 → 统一返回「兔肉」模板
     */
    protected abstract Item getDropTemplate();

    /**
     * 怪物攻击方法
     */
    public abstract void attack(Player player);

    /**
     * 怪物受到伤害，血量最小为0
     */
    public void takeDamage(int atk) {
        this.hp = Math.max(0, this.hp - atk);
    }

    /**
     * 判断怪物是否死亡，如果isDead是真，调用die
     */
    public boolean isDead() {
        return this.hp <= 0;
    }

    /**
     * 怪物死亡，自动掉落物品进背包
     */
    public void die(Player player) {
        System.out.println("【" + this.getName() + "】已被击杀！");
        Item template = getDropTemplate();
        if (template != null) {
            /**copyItem(template)：物品复制方法
             *模板是所有同类型怪物共用的静态对象，如果直接使用，会出现物品数量错乱。
             *每次击杀怪物，都新建一个独立物品对象作为本次掉落物。
             */
            Item dropItem = copyItem(template);
            player.addItem(dropItem);
            System.out.println("成功拾取【" + dropItem.getName() + "】，已存入背包");
        }
    }

    /**
     * 复制物品对象，保证掉落独立
     */
    private Item copyItem(Item template) {
        if (template instanceof Food) {
            Food foodTemplate = (Food) template;//判断对象是不是 Food（食物）类型
            return new Food(
                    foodTemplate.getName(),
                    foodTemplate.getType(),
                    foodTemplate.getEffect(),
                    foodTemplate.getImgPath(),
                    1,
                    foodTemplate.getRecoverType(),
                    foodTemplate.getRecoverValue()
            );
        }
        return null;//传入的模板不是 Food 类型（比如武器、材料），直接返回 null，不再生成掉落物。
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}