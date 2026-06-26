package com.survivalgame.util;

import entity.Player;
import java.io.*;

public class FileUtil {
    private static final FileUtil instance = new FileUtil();
    private static final String SAVE_DIR = "saves";
    private static final String DEFAULT_SAVE_NAME = "gameSave.dat";

    private FileUtil() {}

    public static FileUtil getInstance() {
        return instance;
    }

    // 确保目录存在
    private void ensureDirExists() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public boolean saveGame(Player player) {
        return saveGame(player, DEFAULT_SAVE_NAME);
    }

    public boolean saveGame(Player player, String fileName) {
        if (player == null) return false;
        ensureDirExists();
        if (!fileName.endsWith(".dat")) fileName += ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(SAVE_DIR + File.separator + fileName))) {
            oos.writeObject(player);
            System.out.println("存档成功：" + fileName);
            return true;
        } catch (IOException e) {
            System.err.println("存档失败：" + e.getMessage());
            return false;
        }
    }

    public Player loadGame() {
        return loadGame(DEFAULT_SAVE_NAME);
    }

    public Player loadGame(String fileName) {
        if (!fileName.endsWith(".dat")) fileName += ".dat";
        File file = new File(SAVE_DIR + File.separator + fileName);
        if (!file.exists()) {
            System.out.println("存档不存在：" + fileName);
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Player) {
                return (Player) obj;
            }
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("读档失败：" + e.getMessage());
            return null;
        }
    }

    public boolean deleteSave(String fileName) {
        if (!fileName.endsWith(".dat")) fileName += ".dat";
        File file = new File(SAVE_DIR + File.separator + fileName);
        return file.exists() && file.delete();
    }

    public String[] listSaveFiles() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists() || !dir.isDirectory()) return new String[0];
        return dir.list((d, name) -> name.endsWith(".dat"));
    }

    public boolean hasSave(String fileName) {
        if (!fileName.endsWith(".dat")) fileName += ".dat";
        return new File(SAVE_DIR + File.separator + fileName).exists();
    }
}