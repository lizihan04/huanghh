package com.survivalgame.service;

import java.util.HashMap;
import java.util.Map;

public class RecipeManagement {

    public static final Map<String, Map<String, Integer>> RECIPES = new HashMap<>();

    static {
        // 贝刃：3贝壳 + 2藤蔓
        Map<String, Integer> shellBlade = new HashMap<>();
        shellBlade.put("贝壳", 3);
        shellBlade.put("藤蔓", 2);
        RECIPES.put("贝刃", shellBlade);

        // 石刃：2石头 + 3藤蔓
        Map<String, Integer> stoneBlade = new HashMap<>();
        stoneBlade.put("石头", 2);
        stoneBlade.put("藤蔓", 3);
        RECIPES.put("石刃", stoneBlade);

        // 木棒：3木头 + 2藤蔓
        Map<String, Integer> woodClub = new HashMap<>();
        woodClub.put("木头", 3);
        woodClub.put("藤蔓", 2);
        RECIPES.put("木棒", woodClub);

        // 锤子：2石头 + 1藤蔓
        Map<String, Integer> hammer = new HashMap<>();
        hammer.put("石头", 2);
        hammer.put("藤蔓", 1);
        RECIPES.put("锤子", hammer);

        // 石剑：3石头 + 2藤蔓
        Map<String, Integer> stoneSword = new HashMap<>();
        stoneSword.put("石头", 3);
        stoneSword.put("藤蔓", 2);
        RECIPES.put("石剑", stoneSword);
    }
}