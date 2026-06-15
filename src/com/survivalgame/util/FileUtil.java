package com.survivalgame.util;

import java.io.*;

public class FileUtil {

    /**
     * 保存数据到文件
     * @param fileName 文件名
     * @param content 内容
     */
    public static void saveData(String fileName, String content) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
            System.out.println("文件保存成功: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取文件内容
    public static String loadData(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("文件不存在或读取失败，将开始新游戏。");
            return null;
        }
        return content.toString();
    }
}


