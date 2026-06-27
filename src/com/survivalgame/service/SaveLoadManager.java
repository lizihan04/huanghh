package com.survivalgame.service;

import entity.*;
import com.survivalgame.util.FileUtil;
import java.util.*;

public class SaveLoadManager {

    private static final SaveLoadManager instance = new SaveLoadManager();
    private SaveLoadManager() {}
    public static SaveLoadManager getInstance() {
        return instance;
    }

    private Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    // ===================== 存档 =====================
    public boolean saveGame() {
        return FileUtil.getInstance().saveGame(player);
    }

    public boolean saveGame(String fileName) {
        return FileUtil.getInstance().saveGame(player, fileName);
    }

    // ===================== 读档 =====================
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
        }
        return null;
    }
}