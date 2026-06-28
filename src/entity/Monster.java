package entity;

public abstract class Monster {

    private String name;
    private int life;
    private int attack;
    private Item dropItem;
    private String imgPath;

    // 怪物攻击玩家（子类必须实现）
    public abstract void attack(Player player);

    // 怪物受到伤害
    public void takeDamage(int damage) {
        this.life = Math.max(0, this.life - damage);
    }

    // 判断怪物是否死亡
    public boolean isDead() {
        return this.life <= 0;
    }

    // 获取掉落物品（子类重写）
    protected abstract Item getFixedDrop();

    // 怪物死亡逻辑
    public void die(Player player) {
        System.out.println("【" + this.getName() + "】已被击杀！");
        Item template = getFixedDrop();
        if (template != null) {
            Item newDropItem = copyItem(template, 1);
            if (newDropItem != null) {
                player.addItem(newDropItem.getItemName(), newDropItem.getOwnCount());
                System.out.println("成功拾取【" + newDropItem.getItemName() + "】，已存入背包");
            }
        }
    }

    // 复制物品
    private Item copyItem(Item template, int count) {
        String type = template.getItemType();
        String name = template.getItemName();
        String desc = template.getEffectDesc();
        String img = template.getImgPath();

        switch (type) {
            case "food":
                return new Item(name, type, desc, img,
                        template.getRecoverType(), template.getRecoverValue(), count);
            case "material":
                return new Item(name, type, desc, img, count);
            case "tool":
                return new Item(name, type, desc, img,
                        template.getAttackBonus(), template.getMaxDurability(), count);
            default:
                return null;
        }
    }

    // ========== Getter & Setter ==========
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

    public Item getDropItem() {
        return dropItem;
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