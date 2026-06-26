package com.survivalgame.service;

/**
 * 区域信息类
 * 封装区域名称和对应的背景图片路径
 */
public class AreaInfo {
    private String name;
    private String imagePath;

    public AreaInfo(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}