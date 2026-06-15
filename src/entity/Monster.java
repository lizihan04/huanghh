package entity;

public abstract class Monster {
    // 类图规定私有属性
    private String name;//名字
    private int life;//生命
    private int attack;//攻击
    private Item dropItem;//掉落物品
    private String imgPath;//图片路径

    // 怪物攻击玩家
    public abstract void attack(Player player);

    // 怪物受到伤害
    public void takeDamage(int damage) {
        this.life = Math.max(0, this.life - damage);
    }

    // 获取掉落物品
    public Item drop() {
        return this.dropItem;
    }

    // 判断是否死亡
    public boolean isDead() {
        return this.life <= 0;
    }

    // 返回当前怪物固定掉落物（由子类实现）
    protected abstract Item getFixedDrop();

    // 怪物死亡逻辑：掉落固定物品
    public void die(Player player) {
        System.out.println("【" + this.getName() + "】已被击杀！");
        Item template = getFixedDrop();
        if (template != null) {
            Item newItem = copyItem(template);
            if(newItem != null){
                player.addItem(newItem);
                System.out.println("成功拾取【" + newItem.getName() + "】，已存入背包");
            }
        }
    }

    // 复制物品，防止共用对象错乱
    private Item copyItem(Item template) {
        if (template instanceof Food) {
            Food foodTemplate = (Food) template;
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
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = Math.max(0, life);
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDropItem(Item dropItem) {
        this.dropItem = dropItem;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}