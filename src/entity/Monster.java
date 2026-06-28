package entity;

public abstract class Monster {
    // 私有属性
    private String name;       // 怪物名字
    private int life;          // 生命值
    private int attack;        // 怪物攻击力
    private Item dropItem;     // 掉落物品模板
    private String imgPath;    // 怪物图片路径

    // 怪物攻击玩家
    public abstract void attack(Player player);

    // 怪物承受伤害
    public void takeDamage(int damage) {
        this.life = Math.max(0, this.life - damage);
    }

    // 获取掉落物品模板
    public Item drop() {
        return this.dropItem;
    }

    // 判断怪物是否死亡
    public boolean isDead() {
        return this.life <= 0;
    }

    // 子类重写：获取该怪物固定掉落物品模板
    protected abstract Item getFixedDrop();

    // 怪物死亡逻辑，生成新物品交给玩家拾取
    public void die(Player player) {
        System.out.println("【" + this.getName() + "】已被击杀！");
        Item template = getFixedDrop();
        if (template != null) {
            // 复制物品，数量固定1
            Item newDropItem = copyItem(template, 1);
            if (newDropItem != null) {
                player.addItem(newDropItem.getItemName(), newDropItem.getOwnCount());
                System.out.println("成功拾取【" + newDropItem.getItemName() + "】，已存入背包");
            }
        }
    }

    /**
     * 复制Item对象，根据物品类型调用对应构造，ownCount自定义
     * @param template 模板物品
     * @param count 掉落数量
     * @return 新独立Item对象
     */
    private Item copyItem(Item template, int count) {
        String type = template.getItemType();
        String name = template.getItemName();
        String desc = template.getEffectDesc();
        String img = template.getImgPath();

        switch (type) {
            case "food":
                // 食物构造：name,type,desc,img,recoverType,recoverVal,ownCount
                return new Item(name, type, desc, img,
                        template.getRecoverType(), template.getRecoverValue(), count);
            case "material":
                // 材料构造：name,type,desc,img,ownCount
                return new Item(name, type, desc, img, count);
            case "tool":
                // 工具构造：name,type,desc,img,atkBonus,maxDur,ownCount
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