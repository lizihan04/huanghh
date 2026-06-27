package com.survivalgame.service;

import entity.Player;
import java.util.*;

public class AreaManager {

    private static final AreaManager instance = new AreaManager();
    private AreaManager() {}
    public static AreaManager getInstance() {
        return instance;
    }

    private Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    // ===================== 传送 =====================
    public void switchArea(String targetArea) {
        List<String> areaList = Arrays.asList("沙滩", "树林", "岩石区", "海边");
        if (!areaList.contains(targetArea)) {
            System.out.println("传送失败：区域名称无效");
            return;
        }
        if (player.getActionPoint() <= 0) {
            System.out.println("行动力不足，无法传送！");
            return;
        }
        if (player.getCurrentArea().equals(targetArea)) {
            System.out.println("已经在【" + targetArea + "】");
            return;
        }

        player.doAction(null);
        player.setCurrentArea(targetArea);
        System.out.println("传送至：【" + targetArea + "】");
        player.checkGameOver();
    }

    // ===================== 区域信息 =====================
    public String getCurrentArea() {
        return player.getCurrentArea();
    }

    public AreaInfo getCurrentAreaInfo() {
        return new AreaInfo(player.getCurrentArea(), getAreaImage(player.getCurrentArea()));
    }

    private String getAreaImage(String area) {
        switch (area) {
            case "沙滩": return "images/img_map/map_beach.png";
            case "树林": return "images/img_map/map_forest.png";
            case "岩石区": return "images/img_map/map_rocky.png";
            case "海边": return "images/img_map/map_sea.png";
            default: return "images/img_map/map_beach.png";
        }
    }

    public List<String> getAvailableAreas() {
        return Arrays.asList("沙滩", "树林", "岩石区", "海边");
    }

    public Map<String, String> getAvailableAreasWithImages() {
        Map<String, String> areaMap = new HashMap<>();
        areaMap.put("沙滩", "images/img_map/map_beach.png");
        areaMap.put("树林", "images/img_map/map_forest.png");
        areaMap.put("岩石区", "images/img_map/map_rocky.png");
        areaMap.put("海边", "images/img_map/map_sea.png");
        return areaMap;
    }
}