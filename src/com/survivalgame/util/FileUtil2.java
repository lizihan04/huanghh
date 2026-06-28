package com.survivalgame.util;

import entity.Player2;
import java.io.*;

public class FileUtil2 {
    private static final FileUtil2 instance = new FileUtil2();
    private static final String SAVE_DIR = "saves";
    private static final String DEFAULT_SAVE_NAME = "gameSave.dat";

    private FileUtil2() {}

    public static FileUtil2 getInstance() {
        return instance;
    }

    // 确保存档目录存在
    private void ensureDirExists() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    //存档（默认文件名）
    public boolean saveGame(Player2 player) {
        return saveGame(player, DEFAULT_SAVE_NAME);
    }

    //存档（指定文件名）
    public boolean saveGame(Player2 player, String fileName) {
        if (player == null) {
            System.err.println("存档失败：玩家对象为空");
            return false;
        }
        ensureDirExists();

        if (!fileName.endsWith(".dat")) {
            fileName += ".dat";
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(SAVE_DIR + File.separator + fileName))) {
            oos.writeObject(player);
            System.out.println(" 存档成功：" + fileName);
            return true;
        } catch (IOException e) {
            System.err.println(" 存档失败：" + e.getMessage());
            return false;
        }
    }

    // 读档（默认文件名）
    public Player2 loadGame() {
        return loadGame(DEFAULT_SAVE_NAME);
    }

    //读档（指定文件名）
    public Player2 loadGame(String fileName) {
        if (!fileName.endsWith(".dat")) {
            fileName += ".dat";
        }
        File file = new File(SAVE_DIR + File.separator + fileName);

        if (!file.exists()) {
            System.out.println(" 存档文件不存在：" + fileName);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Player2) {
                System.out.println(" 读档成功：" + fileName);
                return (Player2) obj;
            } else {
                System.err.println(" 存档数据格式异常");
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(" 读档失败：" + e.getMessage());
            return null;
        }
    }

    // 删除存档（默认文件名）
    public boolean deleteSave() {
        return deleteSave(DEFAULT_SAVE_NAME);
    }

    //删除存档（指定文件名）
    public boolean deleteSave(String fileName) {
        if (!fileName.endsWith(".dat")) {
            fileName += ".dat";
        }
        File file = new File(SAVE_DIR + File.separator + fileName);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    // 检查存档是否存在（默认文件名）
    public boolean hasSave() {
        return hasSave(DEFAULT_SAVE_NAME);
    }

    //检查存档是否存在（指定文件名）
    public boolean hasSave(String fileName) {
        if (!fileName.endsWith(".dat")) {
            fileName += ".dat";
        }
        return new File(SAVE_DIR + File.separator + fileName).exists();
    }

    //获取所有存档文件名列表
    public String[] listSaveFiles() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            return new String[0];
        }
        return dir.list((d, name) -> name.endsWith(".dat"));
    }
}
