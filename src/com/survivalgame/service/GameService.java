package com.survivalgame.service;

import entity.*;
import com.survivalgame.util.FileUtil;
import java.util.*;

public class GameService {

    private static final GameService instance = new GameService();
    private GameService() {}
    public static GameService getInstance() {
        return instance;
    }

    private Player player;
    private Random random;
    private Map<String, Terrain> terrains;

    private AreaManager areaManager;
    private BattleManager battleManager;
    private DayManager dayManager;
    private SaveLoadManager saveLoadManager;
    private CraftingManager craftingManager;
    private ExploreManager exploreManager;
    // 删除 RestManager

    // ===================== 初始化 =====================
    public void initGame() {
        this.player = Player.getInstance();
        this.player.initPlayer();
        this.random = new Random();
        initTerrains();
        initManagers();
        player.setCurrentArea("沙滩");
        System.out.println("游戏初始化完成");
    }

    private void initTerrains() {
        terrains = new HashMap<>();
        terrains.put("树林", Forest.getInstance());
        terrains.put("沙滩", Beach.getInstance());
        terrains.put("岩石区", Rocky.getInstance());
        terrains.put("海边", Sea.getInstance());
    }

    private void initManagers() {
        areaManager = AreaManager.getInstance();
        areaManager.setPlayer(player);

        battleManager = BattleManager.getInstance();
        battleManager.setPlayer(player);

        dayManager = DayManager.getInstance();
        dayManager.setPlayer(player);

        saveLoadManager = SaveLoadManager.getInstance();
        saveLoadManager.setPlayer(player);

        craftingManager = CraftingManager.getInstance();
        craftingManager.setPlayer(player);

        exploreManager = ExploreManager.getInstance();
        exploreManager.setPlayer(player);
        exploreManager.setRandom(random);
        exploreManager.setTerrains(terrains);

        // 删除 RestManager 初始化
    }

    // ===================== 灯塔建造 =====================
    public void buildLighthouse() {
        player.buildLighthouse();
        System.out.println("灯塔进度：" + player.getLighthouseProgress() + "%");
        player.checkGameOver();
    }

    // ===================== 休息（直接调用 player.rest()） =====================
    public void rest() {
        if (player.getActionPoint() <= 0) {
            System.out.println("行动力不足，自动进入下一天");
            nextDay();
            return;
        }
        player.rest();
        player.checkGameOver();
    }

    // ===================== 委托给各管理器 =====================

    // 区域
    public void switchArea(String targetArea) { areaManager.switchArea(targetArea); }
    public String getCurrentArea() { return areaManager.getCurrentArea(); }
    public AreaInfo getCurrentAreaInfo() { return areaManager.getCurrentAreaInfo(); }
    public List<String> getAvailableAreas() { return areaManager.getAvailableAreas(); }
    public Map<String, String> getAvailableAreasWithImages() { return areaManager.getAvailableAreasWithImages(); }

    // 战斗
    public void startBattle(Monster monster) { battleManager.startBattle(monster); }

    // 天数
    public void nextDay() { dayManager.nextDay(); }

    // 存档读档
    public boolean saveGame() { return saveLoadManager.saveGame(); }
    public boolean saveGame(String fileName) { return saveLoadManager.saveGame(fileName); }
    public boolean loadGame() { return saveLoadManager.loadGame(); }
    public boolean loadGame(String fileName) { return saveLoadManager.loadGame(fileName); }

    // 工作台
    public String[] getRecipeNames() { return craftingManager.getRecipeNames(); }
    public Map<String, Map<String, Integer>> getAllRecipes() { return craftingManager.getAllRecipes(); }
    public Map<String, Integer> getRecipeDetail(String recipeName) { return craftingManager.getRecipeDetail(recipeName); }
    public boolean craftItem(String recipeName) { return craftingManager.craftItem(recipeName); }

    // 探索
    public void explore() { exploreManager.explore(); }

    // Getter
    public Player getPlayer() { return player; }
}