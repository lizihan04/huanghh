package entity;

/**
 * 背包物品统一对象，3种专属构造器
 * 已删除采集加成，ownCount放入构造参数
 * 方法命名统一：getItemName / getItemType / getEffectDesc / getImgPath
 */
public class Item {
    // 通用基础属性
    private String itemName;       // 物品名称
    private String itemType;       // 分类 food/material/tool
    private String effectDesc;     // 效果描述
    private String imgPath;        // 图片路径

    // 食物专属
    private String recoverType;    // 恢复类型 hp/hunger/fatigue/thirst
    private int recoverValue;      // 恢复数值

    // 工具专属（无采集加成）
    private int attackBonus;       // 攻击加成
    private int maxDurability;     // 最大耐久
    private int currentDurability; // 当前耐久

    // 动态：持有数量（构造传入）
    private int ownCount;

    // 构造1：食物
    public Item(String itemName, String itemType, String effectDesc, String imgPath,
                String recoverType, int recoverValue, int ownCount) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.effectDesc = effectDesc;
        this.imgPath = imgPath;

        this.recoverType = recoverType;
        this.recoverValue = recoverValue;

        this.attackBonus = 0;
        this.maxDurability = 0;
        this.currentDurability = 0;

        this.ownCount = Math.max(0, ownCount);
    }

    // 构造2：材料
    public Item(String itemName, String itemType, String effectDesc, String imgPath, int ownCount) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.effectDesc = effectDesc;
        this.imgPath = imgPath;

        this.recoverType = null;
        this.recoverValue = 0;
        this.attackBonus = 0;
        this.maxDurability = 0;
        this.currentDurability = 0;

        this.ownCount = Math.max(0, ownCount);
    }

    // 构造3：工具（无采集加成）
    public Item(String itemName, String itemType, String effectDesc, String imgPath,
                int attackBonus, int maxDurability, int ownCount) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.effectDesc = effectDesc;
        this.imgPath = imgPath;

        this.recoverType = null;
        this.recoverValue = 0;

        this.attackBonus = attackBonus;
        this.maxDurability = maxDurability;
        this.currentDurability = maxDurability;

        this.ownCount = Math.max(0, ownCount);
    }

    // 增减数量
    public void addCount(int num) {
        if(num > 0) ownCount += num;
    }
    public void reduceCount(int num) {
        ownCount = Math.max(0, ownCount - num);
    }

    // ========== Getter 统一命名（Monster直接调用） ==========
    public String getItemName() {
        return itemName;
    }
    public String getItemType() {
        return itemType;
    }
    public String getEffectDesc() {
        return effectDesc;
    }
    public String getImgPath() {
        return imgPath;
    }
    public String getRecoverType() {
        return recoverType;
    }
    public int getRecoverValue() {
        return recoverValue;
    }
    public int getAttackBonus() {
        return attackBonus;
    }
    public int getMaxDurability() {
        return maxDurability;
    }
    public int getCurrentDurability() {
        return currentDurability;
    }
    public int getOwnCount() {
        return ownCount;
    }

    // Setter
    public void setOwnCount(int ownCount) {
        this.ownCount = Math.max(0, ownCount);
    }
    public void setCurrentDurability(int currentDurability) {
        this.currentDurability = Math.max(0, Math.min(maxDurability, currentDurability));
    }
}