package entity;

/**
 * 单个道具完整数据对象，包含所有属性
 */
public class ItemData {
    // 基础通用属性
    private String itemName;     // 道具名称
    private String itemType;     // 类型 food/material/tool
    private String effectDesc;   // 文字描述效果
    private String imgPath;      // 图片资源路径
    private int count;           //物品数量

    // 食物专属属性
    private String recoverType;  // 恢复类型 hp/hunger/thirst/fatigue
    private int recoverValue;    // 恢复数值

    // 工具专属属性
    private int attackBonus;     // 攻击加成
    private int collectBonus;    // 采集加成
    private int durability;      // 耐久度

    // ========== 构造器 3种 ==========
    // 1. 材料专用构造（无恢复、无工具属性）
    public ItemData(String itemName, String itemType, String effectDesc, String imgPath,int count) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.effectDesc = effectDesc;
        this.imgPath = imgPath;
        this.recoverType = null;
        this.recoverValue = 0;
        this.attackBonus = 0;
        this.collectBonus = 0;
        this.durability = 0;
        this.count = 0;
    }

    // 2. 食物专用构造
    public ItemData(String itemName, String itemType, String effectDesc, String imgPath,
                    String recoverType, int recoverValue,int count) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.effectDesc = effectDesc;
        this.imgPath = imgPath;
        this.recoverType = recoverType;
        this.recoverValue = recoverValue;
        this.attackBonus = 0;
        this.collectBonus = 0;
        this.durability = 0;
        this.count = 0;
    }

    // 3. 工具专用构造
    public ItemData(String itemName, String itemType, String effectDesc, String imgPath,
                    int attackBonus, int collectBonus, int durability,int count) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.effectDesc = effectDesc;
        this.imgPath = imgPath;
        this.recoverType = null;
        this.recoverValue = 0;
        this.attackBonus = attackBonus;
        this.collectBonus = collectBonus;
        this.durability = durability;
        this.count = 0;

    }

    // ========== Getter 全部属性 ==========
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

    public int getCollectBonus() {
        return collectBonus;
    }

    public int getDurability() {
        return durability;
    }

    public int getCount() {return count; }
}