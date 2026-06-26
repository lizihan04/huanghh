package com.survivalgame.service;

import java.util.HashMap;
import java.util.Map;

/**
 * 合成配方数据类
 * 存储所有合成配方
 */
public class RecipeManagement {

    // 配方表：配方名 -> 材料名 -> 数量
    public static final Map<String, Map<String, Integer>> RECIPES = new HashMap<>();

    static {
        // 贝刃：3贝壳 + 2藤蔓（用来开椰子）
        Map<String, Integer> shellBlade = new HashMap<>();
        shellBlade.put("贝壳", 3);
        shellBlade.put("藤蔓", 2);
        RECIPES.put("贝刃", shellBlade);

        // 石刃：2石头 + 3藤蔓
        Map<String, Integer> stoneBlade = new HashMap<>();
        stoneBlade.put("石头", 2);
        stoneBlade.put("藤蔓", 3);
        RECIPES.put("石斧", stoneBlade);

        // 木棒：3树枝 + 2藤蔓
        Map<String, Integer> woodClub = new HashMap<>();
        woodClub.put("树枝", 3);
        woodClub.put("藤蔓", 2);
        RECIPES.put("木棒", woodClub);

        // 锤子：1石头 + 1藤蔓
        Map<String, Integer> hammer = new HashMap<>();
        hammer.put("石头", 1);
        hammer.put("藤蔓", 1);
        RECIPES.put("锤子", hammer);

        // 石剑：3石头 + 2藤蔓
        Map<String, Integer> stoneSword = new HashMap<>();
        stoneSword.put("石头", 3);
        stoneSword.put("藤蔓", 2);
        RECIPES.put("石剑", stoneSword);
    }
}