package com.survivalgame.service;

import entity.*;
import java.util.*;

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

    public boolean craftItem(String recipeName) {
        if (!RecipeManagement.RECIPES.containsKey(recipeName)) {
            System.out.println("未知配方：" + recipeName);
            return false;
        }

        Map<String, Integer> required = RecipeManagement.RECIPES.get(recipeName);

        if (required.containsKey("树枝")) {
            int need = required.get("树枝");
            int have = countMaterialInBackpack("树枝");
            if (have < need) {
                System.out.println("材料不足：树枝 需要 " + need + " 个，当前有 " + have + " 个");
                return false;
            }
        }

        if (required.containsKey("石头")) {
            int need = required.get("石头");
            int have = countMaterialInBackpack("石头");
            if (have < need) {
                System.out.println("材料不足：石头 需要 " + need + " 个，当前有 " + have + " 个");
                return false;
            }
        }

        if (required.containsKey("贝壳")) {
            int need = required.get("贝壳");
            int have = countMaterialInBackpack("贝壳");
            if (have < need) {
                System.out.println("材料不足：贝壳 需要 " + need + " 个，当前有 " + have + " 个");
                return false;
            }
        }

        if (required.containsKey("藤蔓")) {
            int need = required.get("藤蔓");
            int have = countMaterialInBackpack("藤蔓");
            if (have < need) {
                System.out.println("材料不足：藤蔓 需要 " + need + " 个，当前有 " + have + " 个");
                return false;
            }
        }

        for (Map.Entry<String, Integer> entry : required.entrySet()) {
            String materialName = entry.getKey();
            int needCount = entry.getValue();
            List<Item> backpack = player.getBackpack();
            Iterator<Item> it = backpack.iterator();
            int remaining = needCount;
            while (it.hasNext() && remaining > 0) {
                Item item = it.next();
                if (item.getName().equals(materialName) && "material".equals(item.getType())) {
                    int have = item.getCount();
                    if (have <= remaining) {
                        remaining -= have;
                        it.remove();
                    } else {
                        item.setCount(have - remaining);
                        remaining = 0;
                    }
                }
            }
        }

        Tool product = createToolByName(recipeName);
        if (product != null) {
            player.addItem(product);
            System.out.println("合成成功！获得 " + product.getName());
            return true;
        } else {
            System.out.println("合成失败：未知产物");
            return false;
        }
    }

    private int countMaterialInBackpack(String materialName) {
        List<Item> backpack = player.getBackpack();
        int total = 0;
        for (Item item : backpack) {
            if (item.getName().equals(materialName) && "material".equals(item.getType())) {
                total += item.getCount();
            }
        }
        return total;
    }

    private Tool createToolByName(String name) {
        switch (name) {
            case "贝刃":
                return new Tool("贝刃", "weapon", "用来开椰子", "images/img_item/tool/item_shell_blade.png", 1, 15, 0, 15);
            case "石刃":
                return new Tool("石刃", "weapon", "锋利石刃", "images/img_item/tool/item_stone_blade.png", 1, 12, 0, 20);
            case "木棒":
                return new Tool("木棒", "weapon", "近战武器", "images/img_item/tool/item_wood_club.png", 1, 10, 0, 30);
            case "锤子":
                return new Tool("锤子", "weapon", "钝器", "images/img_item/tool/item_hammer.png", 1, 8, 0, 25);
            case "石剑":
                return new Tool("石剑", "weapon", "近战武器", "images/img_item/tool/item_stone_sword.png", 1, 15, 0, 30);
            default:
                return null;
        }
    }
}