package entity;

public abstract class Item {
    private String name;//名字
    private String type;//类型
    private String effect;//影响
    private String imgPath;//图片路径
    private int count;//数量
    // 判断是否为合成材料
    public boolean isMaterial() {
        return "material".equals(this.type);
    }

    // 判断是否为武器
    public boolean isWeapon() {
        return "weapon".equals(this.type);
    }

    //玩家使用
    public abstract void use(Player player);

    public void addCount(int num) {
        System.out.println("拾取" + getName() + num + "个,当前数量：" + getCount());
        this.count += num;
    }

    public void reduceCount1(int num) {
        if (this.count <= 0) {
            System.out.println("【" + getName() + "】数量为0，无需丢弃");
        }
        if (this.count > num) {
            this.count -= num;
            System.out.println("丢弃" + getName() + num + "个，当前数量：" + getCount());
        } else {
            System.out.println("物品数量不足，剩余" + getCount() + "个全部丢弃！当前数量为0");
            this.count = 0;
        }
    }//丢弃

    public void reduceCount2(int num) {
        if (this.count <= 0) {
            System.out.println("【" + getName() + "】数量为0，无法使用");
        }
        if (this.count >= num) {
            this.count -= num;
            System.out.println("使用【" + getName() + "】x" + num + "，剩余数量：" + getCount());
        }//使用
        else {
            this.count = 0;
            System.out.println("数量不足，已用完所有【" + getName() + "】x" + num + "，剩余数量：0");
        }
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Item(String name, String type, String effect, String imgPath, int count) {
        this.name = name;
        this.type = type;
        this.effect = effect;
        this.imgPath = imgPath;
        this.count = count;
    }
}
