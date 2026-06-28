package com.survivalgame.service;

import entity.Item;
import entity.Player;
import java.util.HashMap;
import java.util.Map;

public class CraftingManager {

    private static final CraftingManager instance = new CraftingManager();
    private CraftingManager() {}
    public static CraftingManager getInstance() {
        return instance;
    }

    private Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String[] getRecipeNames() {
        return RecipeManagement.RECIPES.keySet().toArray(new String[0]);
    }

    public Map<String, Map<String, Integer>> getAllRecipes() {
        return RecipeManagement.RECIPES;
    }

    public Map<String, Integer> getRecipeDetail(String recipeName) {
        return RecipeManagement.RECIPES.get(recipeName);
    }

    /**
     * 执行合成
     */
    public boolean craftItem(String recipeName) {
        if (!RecipeManagement.RECIPES.containsKey(recipeName)) {
            System.out.println("未知配方：" + recipeName);
            return false;
        }

        Map<String, Integer> required = RecipeManagement.RECIPES.get(recipeName);

        // 统一循环校验所有材料（删除重复if，通用逻辑）
        for(Map.Entry<String,Integer> entry : required.entrySet()){
            String matName = entry.getKey();
            int need = entry.getValue();
            int have = countMaterialInBackpack(matName);
            if(have < need){
                System.out.println("材料不足：" + matName + " 需要 " + need + " 个，当前有 " + have + " 个");
                return false;
            }
        }

        // 扣除对应材料数量（数组固定下标，只减少ownCount，不置空对象）
        for (Map.Entry<String, Integer> entry : required.entrySet()) {
            String materialName = entry.getKey();
            int deductCount = entry.getValue();
            Item[] backpack = player.getBackpack();
            int remainDeduct = deductCount;

            for (Item item : backpack) {
                if(remainDeduct <= 0) break;
                if (item != null
                        && materialName.equals(item.getItemName())
                        && "material".equals(item.getItemType())) {
                    int own = item.getOwnCount();
                    if (own <= remainDeduct) {
                        remainDeduct -= own;
                        item.setOwnCount(0);
                    } else {
                        item.reduceCount(remainDeduct);
                        remainDeduct = 0;
                    }
                }
            }
        }

        // 生成产物，调用player.addItem(名称,数量)
        if ("灯塔碎片".equals(recipeName)) {
            player.addItem("灯塔碎片", 1);
            System.out.println("合成成功！获得 灯塔碎片");
            return true;
        } else {
            // 根据配方名创建对应工具Item，固定数量1
            String toolName = recipeName;
            Item toolItem = createToolItemByName(toolName);
            if (toolItem != null) {
                player.addItem(toolItem.getItemName(), 1);
                System.out.println("合成成功！获得 " + toolItem.getItemName());
                return true;
            } else {
                System.out.println("合成失败：未知产物");
                return false;
            }
        }
    }

    /**
     * 统计背包内指定材料总数量
     */
    private int countMaterialInBackpack(String materialName) {
        Item[] backpack = player.getBackpack();
        int total = 0;
        for (Item item : backpack) {
            if (item != null
                    && materialName.equals(item.getItemName())
                    && "material".equals(item.getItemType())) {
                total += item.getOwnCount();
            }
        }
        return total;
    }

    /**
     * 创建标准工具Item对象（无Tool子类，统一Item三类构造器）
     */
    private Item createToolItemByName(String name) {
        switch (name) {
            case "贝刃":
                // Item(String name, type, desc, img, attackBonus, maxDur, ownCount)
                return new Item("贝刃", "tool", "攻击+3，无采集加成",
                        "/images/img_item/tool/item_blade.png", 3, 30, 1);
            case "石刃":
                return new Item("石剑", "tool", "攻击+8，无采集加成",
                        "/images/img_item/tool/item_stone_sword.png", 8, 35, 1);
            case "木棒":
                return new Item("木棍", "tool", "攻击+1，无采集加成",
                        "/images/img_item/tool/item_stick.png", 1, 20, 1);
            case "锤子":
                return new Item("锤子", "tool", "攻击+2，矿石采集加成",
                        "/images/img_item/tool/item_hammer.png", 2, 40, 1);
            default:
                return null;
        }
    }
}