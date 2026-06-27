package entity;

/**
 * 全局固定物品配置枚举，对应截图内全部贴图道具
 * 新增工具采集加成、耐久字段与对应getter，匹配Tool完整构造
 */
public enum GameItemConfig {
    // ========== 食物类 food ==========
    COCONUT("椰子", "food", "恢复饥饿", "/images/img_item/food/item_coconut.png", "hunger", 20),
    FISH("鱼", "food", "恢复口渴", "/images/img_item/food/item_fish.png", "thirst", 12),
    PORK("猪肉", "food", "恢复血量", "/images/img_item/food/item_pork.png", "hp", 30),
    RABBIT_MEAT("兔肉", "food", "恢复疲惫", "/images/img_item/food/item_rabbit_meat.png", "fatigue", 22),

    // ========== 材料类 material ==========
    COCONUT_TREE("椰子树", "material", "合成材料", "/images/img_item/material/item_coconut_tree.png"),
    ORE("矿石", "material", "合成材料", "/images/img_item/material/item_ore.png"),
    SHELL("贝壳", "material", "合成材料", "/images/img_item/material/item_shell.png"),
    STONE("石头", "material", "合成材料", "/images/img_item/material/item_stone.png"),
    VINE("藤蔓", "material", "合成材料", "/images/img_item/material/item_vine.png"),
    WOOD("木头", "material", "合成材料", "/images/img_item/material/item_wood.png"),
    TOWER("灯塔碎片", "material", "通关道具", "/images/img_item/tool/item_tower.png"),

    // ========== 工具类 tool ==========
    // 参数：name,type,effect,imgPath,攻击加成,采集加成,初始耐久
    AXE("斧头", "tool", "攻击+4，采集资源加成", "/images/img_item/tool/item_axe.png", 4, 3, 50),
    BLADE("刀刃", "tool", "攻击+3，无采集加成", "/images/img_item/tool/item_blade.png", 3, 0, 30),
    HAMMER("锤子", "tool", "攻击+2，矿石采集加成", "/images/img_item/tool/item_hammer.png", 2, 4, 40),
    STICK("木棍", "tool", "攻击+1，无采集加成", "/images/img_item/tool/item_stick.png", 1, 0, 20),
    STONESWORD0("石剑", "tool", "攻击+8，无采集加成", "/images/img_item/tool/item_stonesword.png", 8, 0, 35);


    // 通用基础字段
    private final String name;
    private final String type;
    private final String effect;
    private final String imgPath;

    // 食物专属属性
    private String recoverType;
    private int recoverValue;

    // 工具专属：攻击加成、采集加成、耐久度
    private int attackBonus;
    private int collectBonus;
    private int durability;

    // ---------------------- 构造器重载 ----------------------
    /**
     * 材料类构造（4参数）
     */
    GameItemConfig(String name, String type, String effect, String imgPath) {
        this.name = name;
        this.type = type;
        this.effect = effect;
        this.imgPath = imgPath;
    }

    /**
     * 食物类构造（7参数，匹配Food构造）
     */
    GameItemConfig(String name, String type, String effect, String imgPath, String recoverType, int recoverValue) {
        this.name = name;
        this.type = type;
        this.effect = effect;
        this.imgPath = imgPath;
        this.recoverType = recoverType;
        this.recoverValue = recoverValue;
    }

    /**
     * 工具类构造（7参数，匹配Tool完整8参构造前7个基础值）
     */
    GameItemConfig(String name, String type, String effect, String imgPath, int attackBonus, int collectBonus, int durability) {
        this.name = name;
        this.type = type;
        this.effect = effect;
        this.imgPath = imgPath;
        this.attackBonus = attackBonus;
        this.collectBonus = collectBonus;
        this.durability = durability;
    }

    // ---------------------- 全部Getter方法（补齐collect、durability，消除红报错） ----------------------
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getEffect() {
        return effect;
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

    // 新增：采集加成getter，修复图片红色报错
    public int getCollectBonus() {
        return collectBonus;
    }

    // 新增：耐久度getter，修复图片红色报错
    public int getDurability() {
        return durability;
    }
}