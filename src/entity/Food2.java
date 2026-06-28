package entity;

public class Food2 extends Item2{
    // 食物专属
    private String recoverType;    // 恢复类型 hp/hunger/fatigue/thirst
    private int recoverValue;

    //构造方法
    public Food2() {
    }

    public Food2(String itemName, int ownCount, String recoverType, int recoverValue) {
        super(itemName, ownCount);
        this.recoverType = recoverType;
        this.recoverValue = recoverValue;
    }

    //getter and setter
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
}
