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

    private Player player;                 // 玩家单例引用
    private MapTile[][] map;               // 网格地图 (30x30)
    private int mapSize = 45;              // 固定45
    private Random random;                 // 随机数生成器
    private Map<String, Map<String, Integer>> recipes;  // 合成配方

    // ---------- 初始化 ----------
    public void initGame() {
        this.player = Player.getInstance();
        this.player.initPlayer();
        this.random = new Random();
        generateMap();
        refreshMapResourcesAndMonsters();
        initRecipes();
        // 设置玩家起始区域为地图中心格子的场景
        int center = (mapSize - 1)/ 2;
        player.setCurrentArea(map[center][center].getSceneType());
        System.out.println("游戏初始化完成，欢迎来到荒岛！");
    }

    // ---------- 地图生成（30x30，边界模糊，陆地面积大） ----------
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
                    Item newResource = createResourceByScene(tile.getSceneType());
                    tile.setResource(newResource);
                }
                if (random.nextDouble() < 0.4) {
                    Monster newMonster = createMonsterByScene(tile.getSceneType());
                    tile.setMonster(newMonster);
                }
            }
        }
    }

    private Item createResourceByScene(String scene) {
        switch (scene) {
            case "树林":
                return new Tool("藤蔓", "material", "合成材料", "img/vine.png", 1 + random.nextInt(3), 0, 0, 0);
            case "沙滩":
                if (random.nextBoolean()) {
                    return new Tool("树枝", "material", "合成材料", "img/stick.png", 1 + random.nextInt(2), 0, 0, 0);
                } else {
                    return new Food("椰子", "food", "解渴", "img/coconut.png", 1, "thirst", 20);
                }
            case "岩石区":
                return new Tool("石头", "material", "合成材料", "img/stone.png", 1 + random.nextInt(3), 0, 0, 0);
            case "海边":
                return new Tool("贝壳", "material", "合成材料", "img/shell.png", 1 + random.nextInt(3), 0, 0, 0);
            default:
                return null;
        }
    }

    private Monster createMonsterByScene(String scene) {
        switch (scene) {
            case "树林":
                return new Monkey();
            case "沙滩":
                // 临时使用 Crab，正式应创建 WildBoar 和 Rabbit
                return new Crab();
            case "岩石区":
                return new BlueSheep();
            case "海边":
                return new TigerShark();
            default:
                return null;
        }
    }

    // ---------- 合成配方 ----------
    private void initRecipes() {
        recipes = new HashMap<>();

        // 木棒：3树枝 + 2藤蔓
        HashMap<String, Integer> woodClub = new HashMap<>();
        woodClub.put("树枝", 3);
        woodClub.put("藤蔓", 2);
        recipes.put("木棒", woodClub);

        // 贝刃：3贝壳 + 2藤蔓
        HashMap<String, Integer> shellBlade = new HashMap<>();
        shellBlade.put("贝壳", 3);
        shellBlade.put("藤蔓", 2);
        recipes.put("贝刃", shellBlade);

        // 石剑：3石头 + 2藤蔓
        HashMap<String, Integer> stoneSword = new HashMap<>();
        stoneSword.put("石头", 3);
        stoneSword.put("藤蔓", 2);
        recipes.put("石剑", stoneSword);

        // 铁剑：3矿石 + 2藤蔓（矿石来自海边）
        HashMap<String, Integer> ironSword = new HashMap<>();
        ironSword.put("矿石", 3);
        ironSword.put("藤蔓", 2);
        recipes.put("铁剑", ironSword);
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
            Monster monster = tile.encounterMonster();  // 需要修正 MapTile 中的 bug
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
            player.attack(monster);   // 需要 Player 中有 attack 方法
            if (monster.isDead()) {
                monster.die(player);
                tile.setMonster(null);
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
        player.rest();
        if (player.getActionPoint() == 0 && !player.isGameOver() && !player.isGameWin()) {
            System.out.println("行动点已用完，请进入下一天！");
        }
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
        Map<String, Integer> required = recipes.get(recipeName);
        if (required == null) {
            System.out.println("未知配方：" + recipeName);
            return false;
        }

        List<Item> backpack = player.getBackpack();
        Map<String, Integer> own = new HashMap<>();
        for (Item item : backpack) {
            if (item.isMaterial()) {
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

    private void removeMaterialFromBackpack(String materialName, int count) {
        List<Item> backpack = player.getBackpack();
        Iterator<Item> it = backpack.iterator();
        while (it.hasNext() && count > 0) {
            Item item = it.next();
            if (item.getName().equals(materialName) && item.isMaterial()) {
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
            case "木棒":
                return new Tool("木棒", "weapon", "近战武器", "img/w_club.png", 1, 10, 0, 30);
            case "贝刃":
                return new Tool("贝刃", "weapon", "近战武器", "img/blade.png", 1, 15, 0, 15);
            case "石剑":
                return new Tool("石剑", "weapon", "近战武器", "img/s_sword.png", 1, 15, 0, 30);
            case "铁剑":
                return new Tool("铁剑", "weapon", "近战武器", "img/i_sword.png", 1, 30, 0, 45);
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
        if (this.map == null || this.map.length != 30) {
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
        } else {
            // 如果传入其他类型，返回 null
            return null;
        }
    }

    // ---------- Getter ----------
    public Player getPlayer() { return player; }
    public MapTile[][] getMap() { return map; }
    public int getMapSize() { return mapSize; }
    public Map<String, Map<String, Integer>> getRecipes() { return recipes; }
    public MapTile getTile(int row, int col) {
        if (row >= 0 && row < mapSize && col >= 0 && col < mapSize) return map[row][col];
        return null;
    }
}