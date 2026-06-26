package com.survivalgame.util;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片加载工具类 ImageUtil
 * 单例模式全局唯一，专门读取resources目录下的游戏图片资源
 * 内置缓存机制，避免重复读取同一张图片造成卡顿、浪费内存
 * 适配本项目分层图片目录：地图、怪物、道具(材料/食物/工具)
 */
public class ImageUtil {
    // 单例静态实例，全局只创建一份工具对象
    private static ImageUtil instance;

    // 图片缓存容器：key=资源完整路径字符串  value=图片对象
    private final Map<String, Image> imageCache;

    /**
     * 私有构造方法，禁止外部使用new创建实例，保证单例
     */
    private ImageUtil() {
        // 初始化缓存容器
        imageCache = new HashMap<>();
    }

    /**
     * 获取全局唯一的ImageUtil工具实例
     * @return 工具单例对象
     */
    public static ImageUtil getInstance() {
        // 懒加载：第一次使用时才创建对象
        if (instance == null) {
            instance = new ImageUtil();
        }
        return instance;
    }

    /**
     * 根据资源路径加载图片，自动缓存复用
     * @param resPath resources内资源路径，路径开头必须带 /
     * 示例路径：
     * /images/img_map/map_beach.png        沙滩地图
     * /images/img_monster/monster_shark.png 鲨鱼怪物
     * /images/img_item/material/item_ore.png 矿石材料
     * /images/img_item/tool/item_stonesword.png 石剑武器
     * @return 加载成功返回Image图片对象；加载失败返回null并打印错误日志
     */
    public Image loadImage(String resPath) {
        // 1. 先判断缓存里是否已有该图片，有则直接返回，不重复加载
        if (imageCache.containsKey(resPath)) {
            return imageCache.get(resPath);
        }

        try {
            // 2. 通过类加载器读取resources下的图片流
            Image targetImage = new Image(getClass().getResourceAsStream(resPath));
            // 3. 将新图片存入缓存，下次调用直接复用
            imageCache.put(resPath, targetImage);
            return targetImage;
        } catch (Exception exception) {
            // 捕获路径错误、文件缺失、格式损坏等异常
            System.err.println("【图片加载失败】资源路径：" + resPath);
            System.err.println("异常原因：" + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * 清空全部图片缓存
     * 使用场景：重新开局、切换大地图、重置游戏时调用，释放内存占用
     */
    public void clearCache() {
        imageCache.clear();
        System.out.println("图片缓存已全部清空");
    }
}