package com.survivalgame.service;

import entity.*;
import com.survivalgame.util.FileUtil;

import java.util.*;

/**
 * 游戏核心业务逻辑服务类（单例）
 * 负责：区域管理、传送、探索、休息、战斗、合成、天数管理、存档读档
 */
public class GameService {

    private static final GameService instance = new GameService();
    private GameService() {}

    public static GameService getInstance() {
        return instance;
    }

    private Player player;
    private Random random;
    private Map<String, Terrain> terrains;

    // ===================== 初始化 =====================
    public void initGame() {
        this.player = Player.getInstance();
        this.player.initPlayer();
        this.random = new Random();
        initTerrains();
        player.setCurrentArea("沙滩");
        System.out.println("游戏初始化完成，欢迎来到荒岛！你出生在沙滩。");
    }

    // ===================== 地形服务 =====================
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

    // ===================== 传送功能 =====================
    public void switchArea(String targetArea) {
        List<String> areaList = Arrays.asList("沙滩", "树林", "岩石区", "海边");
        if (!areaList.contains(targetArea)) {
            System.out.println("传送失败：区域名称无效！可选：沙滩、树林、岩石区、海边");
            return;
        }
        if (player.isGameOver() || player.isGameWin()) {
            System.out.println("游戏已结束，无法传送！");
            return;
        }
        if (player.getActionPoint() <= 0) {
            System.out.println("行动力不足，无法传送！");
            return;
        }
        if (player.getCurrentArea().equals(targetArea)) {
            System.out.println("你已经在【" + targetArea + "】，无需重复传送");
            return;
        }

        player.doAction(null);
        player.setCurrentArea(targetArea);
        System.out.println("传送成功！当前位置：【" + targetArea + "】");
        player.checkGameOver();
    }

    // ===================== 探索功能 =====================
    public void explore() {
        if (player.isGameOver() || player.isGameWin()) {
            System.out.println("游戏已结束，无法探索");
            return;
        }
        if (player.getActionPoint() <= 0) {
            System.out.println("行动力不足，无法探索！自动进入下一天");
            nextDay();
            return;
        }

        player.doAction(null);
        Terrain terrain = getTerrain(player.getCurrentArea());
        double r = random.nextDouble();

        if (r < 0.4) {
            Item resource = terrain.createResource();
            if (resource != null) {
                player.addItem(resource);
                System.out.println("在【" + player.getCurrentArea() + "】探索，发现 " + resource.getName() + " x" + resource.getCount());
            } else {
                System.out.println("探索一番，什么也没发现");
            }
        } else if (r < 0.8) {
            Monster monster = terrain.createMonster();
            if (monster != null) {
                System.out.println("在【" + player.getCurrentArea() + "】探索，遭遇 " + monster.getName() + "！");
                startBattle(monster);
            } else {
                System.out.println("探索一番，没有发现怪物");
            }
        } else {
            System.out.println("在【" + player.getCurrentArea() + "】探索了一番，什么也没有发生");
        }
        player.checkGameOver();
    }

    // ===================== 休息功能 =====================
    public void rest() {
        if (player.isGameOver() || player.isGameWin()) {
            System.out.println("游戏已结束，无法休息");
            return;
        }
        if (player.getActionPoint() <= 0) {
            System.out.println("行动力不足，自动进入下一天");
            nextDay();
            return;
        }
        player.rest();
        Terrain terrain = getTerrain(player.getCurrentArea());
        String effect = terrain.getRestEffect(player);
        System.out.println(effect);
        player.checkGameOver();
    }

    // ===================== 战斗系统 =====================
    private void startBattle(Monster monster) {
        while (!monster.isDead() && player.getHp() > 0) {
            boolean killSuccess = player.attackMonster(monster);
            if (killSuccess) {
                monster.die(player);
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

    // ===================== 天数管理 =====================
    public void nextDay() {
        if (player.isGameOver() || player.isGameWin()) {
            System.out.println("游戏已结束");
            return;
        }
        player.nextDay();
        System.out.println("===== 第 " + player.getDay() + " 天 =====");
        System.out.println("行动点已重置为10");
    }

    // ===================== 灯塔建造 =====================
    public void buildLighthouse() {
        if (player.isGameOver() || player.isGameWin()) {
            System.out.println("游戏已结束");
            return;
        }
        player.buildLighthouse();
        System.out.println("灯塔建造进度：" + player.getLighthouseProgress() + "%");
        player.checkGameOver();
    }

    // ===================== 合成系统 =====================
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

    // ===================== 获取区域信息（供UI调用） =====================
    public String getCurrentArea() {
        return player.getCurrentArea();
    }

    public AreaInfo getCurrentAreaInfo() {
        String area = player.getCurrentArea();
        String imagePath = getAreaImage(area);
        return new AreaInfo(area, imagePath);
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

    // ===================== 存档读档 =====================
    public boolean saveGame() {
        return FileUtil.getInstance().saveGame(player);
    }

    public boolean saveGame(String fileName) {
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

    // ===================== Getter =====================
    public Player getPlayer() {
        return player;
    }
}