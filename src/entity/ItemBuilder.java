package entity;

import entity.Food;
import entity.GameItemConfig;
import entity.Item;
import entity.Material;
import entity.Tool;

/**
 * 道具生成工厂，全局唯一生成道具入口
 * 适配新版Tool：攻击加成、采集加成、耐久度三属性完整传参
 * 解决抽象Item无法实例化、构造参数数量不匹配编译报错
 */
public class ItemBuilder {
    // 单例静态实例
    private static final ItemBuilder instance = new ItemBuilder();

    // 私有构造，禁止外部new工厂对象
    private ItemBuilder() {}

    /**
     * 获取工厂单例
     * @return ItemBuilder唯一实例
     */
    public static ItemBuilder getInstance() {
        return instance;
    }

    /**
     * 根据预设枚举配置生成对应道具子类对象
     * @param config 物品预设枚举GameItemConfig
     * @param count 生成道具数量
     * @return Item抽象父类（Material / Food / Tool）
     */
    public Item createItem(GameItemConfig config, int count) {
        // 非法参数直接返回空对象，不生成道具
        if (config == null || count <= 0) {
            return null;
        }

        switch (config.getType()) {
            case "material":
                // 材料：使用Material子类，不能直接new抽象Item
                return new Material(
                        config.getName(),
                        config.getType(),
                        config.getEffect(),
                        config.getImgPath(),
                        count
                );
            case "food":
                // 食物：7个参数完整匹配Food构造方法
                return new Food(
                        config.getName(),
                        config.getType(),
                        config.getEffect(),
                        config.getImgPath(),
                        count,
                        config.getRecoverType(),
                        config.getRecoverValue()
                );
            case "tool":
                // 工具：8个参数完整匹配Tool构造，顺序严格对齐
                return new Tool(
                        config.getName(),
                        config.getType(),
                        config.getEffect(),
                        config.getImgPath(),
                        count,
                        config.getAttackBonus(),
                        config.getCollectBonus(),
                        config.getDurability()
                );
            default:
                // 未知物品类型，返回空
                return null;
        }
    }
}