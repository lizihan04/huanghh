package util;

import entity.Player;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件存档工具类（单例）
 * 功能：保存/加载游戏进度（基于 Java 序列化），支持多存档文件
 */
public class FileUtil {

    // 单例对象
    private static final FileUtil instance = new FileUtil();

    // 默认存档目录（项目根目录下的 saves 文件夹）
    private static final String SAVE_DIR = "saves";

    // 默认存档文件名（不含路径）
    private static final String DEFAULT_SAVE_NAME = "gameSave.dat";

    // 静态代码块：确保存档目录存在
    static {
        try {
            Path dir = Paths.get(SAVE_DIR);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            System.err.println("创建存档目录失败：" + e.getMessage());
        }
    }

    // 私有构造，禁止外部实例化
    private FileUtil() {}

    /**
     * 获取单例对象
     * @return FileUtil 实例
     */
    public static FileUtil getInstance() {
        return instance;
    }

    /**
     * 保存游戏存档（使用默认文件名）
     * @param player 玩家对象（包含所有游戏数据）
     * @return 保存是否成功
     */
    public boolean saveGame(Player player) {
        return saveGame(player, DEFAULT_SAVE_NAME);
    }

    /**
     * 保存游戏存档（指定文件名）
     * @param player   玩家对象
     * @param fileName 存档文件名（例如 "save1.dat"），会自动加上 .dat 后缀（如果没有）
     * @return 保存是否成功
     */
    public boolean saveGame(Player player, String fileName) {
        if (player == null) {
            System.err.println("存档失败：玩家对象为空");
            return false;
        }

        // 规范化文件名：确保以 .dat 结尾
        if (!fileName.endsWith(".dat")) {
            fileName += ".dat";
        }

        Path filePath = Paths.get(SAVE_DIR, fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(filePath)))) {
            oos.writeObject(player);
            System.out.println("游戏存档成功：" + filePath.toAbsolutePath());
            return true;
        } catch (IOException e) {
            System.err.println("存档失败：" + e.getMessage());
            e.printStackTrace(); // 开发阶段保留堆栈，正式版可删除
            return false;
        }
    }

    /**
     * 加载游戏存档（使用默认文件名）
     * @return 玩家对象，如果无存档或读取失败则返回 null
     */
    public Player loadGame() {
        return loadGame(DEFAULT_SAVE_NAME);
    }

    /**
     * 加载游戏存档（指定文件名）
     * @param fileName 存档文件名
     * @return 玩家对象，如果文件不存在或读取失败则返回 null
     */
    public Player loadGame(String fileName) {
        if (!fileName.endsWith(".dat")) {
            fileName += ".dat";
        }

        Path filePath = Paths.get(SAVE_DIR, fileName);
        if (!Files.exists(filePath)) {
            System.out.println("存档文件不存在：" + fileName);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(filePath)))) {
            Object obj = ois.readObject();
            if (obj instanceof Player) {
                System.out.println("读取存档成功：" + fileName);
                return (Player) obj;
            } else {
                System.err.println("存档数据格式异常，不是 Player 对象");
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("读取存档失败：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除默认存档文件
     * @return 是否删除成功
     */
    public boolean deleteSave() {
        return deleteSave(DEFAULT_SAVE_NAME);
    }

    /**
     * 删除指定的存档文件
     * @param fileName 存档文件名
     * @return 是否删除成功
     */
    public boolean deleteSave(String fileName) {
        if (!fileName.endsWith(".dat")) {
            fileName += ".dat";
        }
        Path filePath = Paths.get(SAVE_DIR, fileName);
        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("删除存档失败：" + e.getMessage());
            return false;
        }
    }

    /**
     * 获取所有存档文件名称列表
     * @return 存档文件名数组（如 ["save1.dat", "save2.dat"]）
     */
    public String[] listSaveFiles() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            return new String[0];
        }
        return dir.list((d, name) -> name.endsWith(".dat"));
    }

    /**
     * 检查是否存在指定存档
     * @param fileName 存档文件名
     * @return 是否存在
     */
    public boolean hasSave(String fileName) {
        if (!fileName.endsWith(".dat")) {
            fileName += ".dat";
        }
        return Files.exists(Paths.get(SAVE_DIR, fileName));
    }
}