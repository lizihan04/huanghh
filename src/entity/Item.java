package entity;

public abstract class Item {
    private String name;//名字
    private String type;//类型
    private String effect;//影响
    private String imgPath;//图片路径
    private int count;//数量

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


    public abstract void use(Player player);//玩家使用
    public void addCount(int num){
        System.out.println("拾取"+getName()+num+"个");
    }
    public  void reduceCount1(int num){
        System.out.println("丢弃"+getName()+num+"个");
    }//丢弃
    public  void reduceCount2(int num){
        System.out.println("使用"+getName()+num+"个");
    }//使用
}
