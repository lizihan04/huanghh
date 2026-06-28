package com.survivalgame.service;

import entity.Item;
import entity.Player;
import com.survivalgame.util.FileUtil;

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

    /**
     * 覆盖当前玩家所有数据
     */
    private void copyPlayerData(Player loaded) {
        Player current = Player.getInstance();
        // 基础生存属性
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

        // 背包覆盖：原背包数组全部置0，再把读档数据同步数量
        Item[] currentBag = current.getBackpack();
        Item[] loadedBag = loaded.getBackpack();

        // 1. 清空当前背包所有物品数量
        for(Item item : currentBag){
            if(item != null) item.setOwnCount(0);
        }
        // 2. 同步读档背包数量（下标一一对应）
        for(int i = 0; i < loadedBag.length; i++){
            Item loadItem = loadedBag[i];
            if(loadItem != null && currentBag[i] != null){
                currentBag[i].setOwnCount(loadItem.getOwnCount());
                // 同步工具耐久
                if("tool".equals(loadItem.getItemType())){
                    currentBag[i].setCurrentDurability(loadItem.getCurrentDurability());
                }
            }
        }
        current.checkGameOver();
    }

    /**
     * 复制Item对象（适配新版单一Item类，无Food/Tool子类）
     */
    private Item copyItem(Item src) {
        if(src == null) return null;
        String name = src.getItemName();
        String type = src.getItemType();
        String desc = src.getEffectDesc();
        String img = src.getImgPath();
        int count = src.getOwnCount();

        switch (type) {
            case "food":
                return new Item(name, type, desc, img,
                        src.getRecoverType(), src.getRecoverValue(), count);
            case "material":
                return new Item(name, type, desc, img, count);
            case "tool":
                return new Item(name, type, desc, img,
                        src.getAttackBonus(), src.getMaxDurability(), count);
            default:
                return null;
        }
    }
}