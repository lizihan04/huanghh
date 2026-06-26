package com.survivalgame.service;

import entity.*;
import com.survivalgame.util.FileUtil;

import java.util.*;

/**
 * 游戏核心业务逻辑服务类（单例）
 * 负责：地图生成、每日刷新、探索事件、战斗系统、合成系统、天数管理、存档读档
 */
public class GameService {

    private static final GameService instance = new GameService();
    private GameService() {}

    public static GameService getInstance() {
        return instance;
    }

    private Player player;
    private MapTile[][] map;
    private final int mapSize = 45;
    private Random random;
    private Map<String, Terrain> terrains;  // ← 新增：地形映射

    // ---------- 初始化 ----------
    public void initGame() {
        this.player = Player.getInstance();
        this.player.initPlayer();
        this.random = new Random();
        initTerrains();
        generateMap();
        refreshMapResourcesAndMonsters();
        // 玩家出生在沙滩
        player.setCurrentArea("沙滩");
        System.out.println("游戏初始化完成，欢迎来到荒岛！你出生在沙滩。");
    }

    // ---------- 地形服务 ----------
    private void initTerrains() {
        terrains = new HashMap<>();
        terrains.put("树林", Forest.getInstance());
        terrains.put("沙滩", Beach.getInstance());
        terrains.put("岩石区", Rocky.getInstance());
        terrains.put("海边", Sea.getInstance());
    }

    private Terrain getTerrain(String sceneType) {
        Terrain terrain = terrains.get(sceneType);
        if (terrain == null) {
            System.err.println("警告：未知地形 '" + sceneType + "'，使用沙滩作为默认");
            return terrains.get("沙滩");
        }
        return terrain;
    }

    // ===================== 玩家主动切换区域 =====================
    public void switchArea(String targetArea) {
        List<String> areaList = Arrays.asList("沙滩", "树林", "岩石区", "海边");
        if (!areaList.contains(targetArea)) {
            System.out.println("切换失败：区域名称无效！可选：沙滩、树林、岩石区、海边");
            return;
        }
        if (player.isGameOver() || player.isGameWin()) {
            System.out.println("游戏已结束，无法切换区域！");
            return;
        }
        if (player.getActionPoint() <= 0) {
            System.out.println("行动力不足，无法前往新区域！");
            return;
        }
        if (player.getCurrentArea().equals(targetArea)) {
            System.out.println("你当前已经在【" + targetArea + "】，无需重复切换");
            return;
        }

        player.doAction(null);
        player.setCurrentArea(targetArea);
        System.out.println("成功移动至：" + targetArea);

        if (random.nextDouble() < 0.3) {
            Monster monster = getTerrain(targetArea).createMonster();
            if (monster != null) {
                System.out.println("抵达" + targetArea + "，遭遇怪物：" + monster.getName() + "！");
                startBattle(monster, null);
            }
        }
    }

    // ===================== 获取区域信息（供UI调用） =====================
    /**
     * 获取玩家当前所在区域
     */
    public String getCurrentArea() {
        return player.getCurrentArea();
    }

    /**
     * 获取玩家当前区域信息（区域名 + 对应的背景图片路径）
     */
    public AreaInfo getCurrentAreaInfo() {
        String area = player.getCurrentArea();
        String imagePath = getAreaImage(area);
        return new AreaInfo(area, imagePath);
    }

    /**
     * 根据区域名称获取对应的背景图片路径
     */
    private String getAreaImage(String area) {
        switch (area) {
            case "沙滩":
                return "images/img_map/map_beach.png";
            case "树林":
                return "images/img_map/map_forest.png";
            case "岩石区":
                return "images/img_map/map_rocky.png";
            case "海边":
                return "images/img_map/map_sea.png";
            default:
                return "images/img_map/map_beach.png";
        }
    }

    /**
     * 获取所有可用区域列表（供UI显示传送按钮）
     */
    public List<String> getAvailableAreas() {
        return Arrays.asList("沙滩", "树林", "岩石区", "海边");
    }

    /**
     * 获取所有可用区域及其对应的图片路径
     */
    public Map<String, String> getAvailableAreasWithImages() {
        Map<String, String> areaMap = new HashMap<>();
        areaMap.put("沙滩", "images/img_map/map_beach.png");
        areaMap.put("树林", "images/img_map/map_forest.png");
        areaMap.put("岩石区", "images/img_map/map_rocky.png");
        areaMap.put("海边", "images/img_map/map_sea.png");
        return areaMap;
    }


    // ---------- 地图生成 ----------
    private void generateMap() {
        map = new MapTile[mapSize][mapSize];
        double center = (mapSize - 1) / 2.0;

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                double dist = Math.sqrt(Math.pow(i - center, 2) + Math.pow(j - center, 2));

                String sceneType;
                if (dist < 8) {
                    sceneType = "树林";
                } else if (dist < 19) {
                    sceneType = "沙滩";
                } else if (dist < 35) {
                    sceneType = "岩石区";
                } else {
                    sceneType = "海边";
                }

                String imgPath = "img/tile_" + sceneType + ".png";
                MapTile tile = new MapTile(i, j, sceneType, imgPath);
                map[i][j] = tile;
            }
        }
        player.setGameMap(map);
    }

    // ---------- 刷新所有格子的资源与怪物 ----------
    public void refreshMapResourcesAndMonsters() {
        for (MapTile[] row : map) {
            for (MapTile tile : row) {
                tile.setResource(null);
                tile.setMonster(null);

                if (random.nextDouble() < 0.4) {
                    Item newResource = getTerrain(tile.getSceneType()).createResource();
                    tile.setResource(newResource);
                }
                if (random.nextDouble() < 0.4) {
                    Monster newMonster = getTerrain(tile.getSceneType()).createMonster();
                    tile.setMonster(newMonster);
                }
            }
        }
    }

    // ---------- 探索格子 ----------
    public void exploreTile(int row, int col) {
        if (row < 0 || row >= mapSize || col < 0 || col >= mapSize) {
            System.out.println("坐标无效");
            return;
        }
        if (player.isGameOver() || player.isGameWin()) {
            System.out.println("游戏已结束");
            return;
        }
        if (player.getActionPoint() <= 0) {
            System.out.println("行动点已用完，请进入下一天！");
            return;
        }

        player.doAction(null);
        MapTile tile = map[row][col];
        player.setCurrentArea(tile.getSceneType());

        double r = random.nextDouble();
        if (r < 0.4) {
            Item resource = tile.collectResource();
            if (resource != null) {
                player.addItem(resource);
                System.out.println("在" + tile.getSceneType() + "发现 " + resource.getName() + " x" + resource.getCount());
            } else {
                System.out.println("这里没有物资");
            }
        } else if (r < 0.8) {
            Monster monster = tile.encounterMonster();
            if (monster != null) {
                System.out.println("遭遇 " + monster.getName() + "！");
                startBattle(monster, tile);
            } else {
                System.out.println("这里没有怪物出没");
            }
        } else {
            System.out.println("探索一番，什么也没发生");
        }

        if (player.getActionPoint() == 0 && !player.isGameOver() && !player.isGameWin()) {
            System.out.println("行动点已用完，请进入下一天！");
        }
        player.checkGameOver();
    }

    // ---------- 回合制战斗 ----------
    private void startBattle(Monster monster, MapTile tile) {
        while (!monster.isDead() && player.getHp() > 0) {
            boolean killSuccess = player.attackMonster(monster);
            if (killSuccess) {
                monster.die(player);
                if (tile != null) tile.setMonster(null);
                System.out.println("战斗胜利！");
                break;
            }
            monster.attack(player);
            if (player.getHp() <= 0) {
                System.out.println("战斗失败，玩家死亡");
                player.checkGameOver();
                break;
            }
        }
    }

    // ---------- 休息 ----------
    public void rest() {
        if (player.isGameOver() || player.isGameWin()) {
            System.out.println("游戏已结束");
            return;
        }
        if (player.getActionPoint() <= 0) {
            System.out.println("行动点已用完，请进入下一天！");
            return;
        }
        // 使用地形类的休息效果
        Terrain terrain = getTerrain(player.getCurrentArea());
        String effect = terrain.getRestEffect(player);
        System.out.println(effect);
        player.checkGameOver();
    }

    // ---------- 进入下一天 ----------
    public void nextDay() {
        if (player.isGameOver() || player.isGameWin()) {
            System.out.println("游戏已结束");
            return;
        }
        player.nextDay();
        refreshMapResourcesAndMonsters();
        System.out.println("进入第 " + player.getDay() + " 天，行动点重置为10");
    }

    // ---------- 建造灯塔 ----------
    public void buildLighthouse() {
        if (player.isGameOver() || player.isGameWin()) {
            System.out.println("游戏已结束");
            return;
        }
        player.buildLighthouse();
        System.out.println("灯塔建造进度：" + player.getLighthouseProgress() + "%");
        player.checkGameOver();
    }

    // ---------- 合成物品 ----------
    public boolean craftItem(String recipeName) {
        if (player.isGameOver() || player.isGameWin()) {
            System.out.println("游戏已结束，无法合成");
            return false;
        }
        Map<String, Integer> required = RecipeManagement.RECIPES.get(recipeName);
        if (required == null) {
            System.out.println("未知配方：" + recipeName);
            return false;
        }

        List<Item> backpack = player.getBackpack();
        Map<String, Integer> own = new HashMap<>();
        for (Item item : backpack) {
            if (item.getType().equals("material")) {
                own.put(item.getName(), own.getOrDefault(item.getName(), 0) + item.getCount());
            }
        }
        for (Map.Entry<String, Integer> entry : required.entrySet()) {
            String mat = entry.getKey();
            int need = entry.getValue();
            if (own.getOrDefault(mat, 0) < need) {
                System.out.println("材料不足：" + mat + " 需要 " + need);
                return false;
            }
        }

        for (Map.Entry<String, Integer> entry : required.entrySet()) {
            String mat = entry.getKey();
            int need = entry.getValue();
            removeMaterialFromBackpack(mat, need);
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

    // ---------- 合成工具方法 ----------
    private void removeMaterialFromBackpack(String materialName, int count) {
        List<Item> backpack = player.getBackpack();
        Iterator<Item> it = backpack.iterator();
        while (it.hasNext() && count > 0) {
            Item item = it.next();
            if (item.getName().equals(materialName) && item.getType().equals("material")) {
                int have = item.getCount();
                if (have <= count) {
                    count -= have;
                    it.remove();
                } else {
                    item.setCount(have - count);
                    count = 0;
                }
            }
        }
        if (count > 0) {
            System.out.println("警告：移除材料时数量不足，请检查逻辑");
        }
    }

    private Tool createToolByName(String name) {
        switch (name) {
            case "贝刃":
                return new Tool("贝刃", "weapon", "用来开椰子", "img/shell_blade.png", 1, 15, 0, 15);
            case "石刃":
                return new Tool("石斧", "weapon", "锋利石刃", "img/stone_blade.png", 1, 12, 0, 20);
            case "木棒":
                return new Tool("木棒", "weapon", "近战武器", "img/w_club.png", 1, 10, 0, 30);
            case "锤子":
                return new Tool("锤子", "weapon", "钝器", "img/hammer.png", 1, 8, 0, 25);
            case "石剑":
                return new Tool("石剑", "weapon", "近战武器", "img/s_sword.png", 1, 15, 0, 30);
            default:
                return null;
        }
    }

    // ---------- 存档读档 ----------
    public boolean saveGame() {
        player.setGameMap(map);
        return FileUtil.getInstance().saveGame(player);
    }

    public boolean saveGame(String fileName) {
        player.setGameMap(map);
        return FileUtil.getInstance().saveGame(player, fileName);
    }

    public boolean loadGame() {
        Player loaded = FileUtil.getInstance().loadGame();
        return loadGameInternal(loaded);
    }

    public boolean loadGame(String fileName) {
        Player loaded = FileUtil.getInstance().loadGame(fileName);
        return loadGameInternal(loaded);
    }

    private boolean loadGameInternal(Player loaded) {
        if (loaded == null) return false;
        copyPlayerData(loaded);
        this.map = player.getGameMap();
        if (this.map == null || this.map.length != mapSize) {
            System.err.println("存档地图数据异常");
            return false;
        }
        this.random = new Random();
        System.out.println("游戏加载成功");
        return true;
    }

    private void copyPlayerData(Player loaded) {
        Player current = Player.getInstance();
        current.setHp(loaded.getHp());
        current.setHunger(loaded.getHunger());
        current.setThirst(loaded.getThirst());
        current.setFatigue(loaded.getFatigue());
        current.setActionPoint(loaded.getActionPoint());
        current.setDay(loaded.getDay());
        current.setCurrentArea(loaded.getCurrentArea());
        current.setBaseAttack(loaded.getBaseAttack());
        current.setDefense(loaded.getDefense());
        current.setLighthouseProgress(loaded.getLighthouseProgress());
        List<Item> newBackpack = new ArrayList<>();
        for (Item item : loaded.getBackpack()) {
            newBackpack.add(copyItem(item));
        }
        current.getBackpack().clear();
        current.getBackpack().addAll(newBackpack);
        current.setGameMap(loaded.getGameMap());
        current.checkGameOver();
    }

    private Item copyItem(Item src) {
        if (src instanceof Food) {
            Food f = (Food) src;
            return new Food(f.getName(), f.getType(), f.getEffect(), f.getImgPath(),
                    f.getCount(), f.getRecoverType(), f.getRecoverValue());
        } else if (src instanceof Tool) {
            Tool t = (Tool) src;
            return new Tool(t.getName(), t.getType(), t.getEffect(), t.getImgPath(),
                    t.getCount(), t.getAttackBonus(), t.getCollectBonus(), t.getDurability());
        } else if (src instanceof Clip) {
            Clip c = (Clip) src;
            return new Clip(c.getName(), c.getType(), c.getEffect(), c.getImgPath(),
                    c.getCount(), c.getClipId());
        } else {
            return null;
        }
    }

    // ---------- Getter ----------
    public Player getPlayer() { return player; }
    public MapTile[][] getMap() { return map; }
    public int getMapSize() { return mapSize; }
    public MapTile getTile(int row, int col) {
        if (row >= 0 && row < mapSize && col >= 0 && col < mapSize) return map[row][col];
        return null;
    }
}