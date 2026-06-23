package entity;

import java.util.Random;
public class MapTile {
    //地图碎片属性
    // 格子行坐标
    private int row;
    // 格子列坐标
    private int col;
    // 场景类型：沙滩、树林、岩石区、海边
    private String sceneType;
    // 是否拥有资源
    private boolean hasResource;
    // 格子内的资源（物品：食物/工具）
    private Item resource;
    // 是否拥有怪物
    private boolean hasMonster;
    // 格子内的怪物（野兽）
    private Monster monster;
    // 格子图片路径
    private String imgPath;

    //全参构造方法
    public MapTile(int row, int col, String sceneType,String imgPath) {
        this.row = row;
        this.col = col;
        this.sceneType = sceneType;
        this.imgPath = imgPath;
        // 初始化默认状态
        this.hasResource = false;
        this.hasMonster = false;
        this.resource = null;
        this.monster = null;
    }

    //getter和setter方法
    public Item getResource() {
        return resource;
    }

    public void setResource(Item resource) {
        this.resource = resource;
        this.hasResource = (resource != null);
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
        this.hasMonster = (monster != null);
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isHasResource() {
        return hasResource;
    }

    public boolean isHasMonster() {
        return hasMonster;
    }

    //收集当前格子资源
        public Item collectResource() {
            // 检验：存在标记且资源对象不为空，才可以采集
            if (this.hasResource && this.resource != null) {
                Item temp1 = this.resource;
                // 清空格子资源，内部自动同步hasResource标记
                this.setResource(null);
                return temp1;
            }
            return null;
        }

        //遭遇怪物
        public Monster encounterMonster() {
            // 检验：存在标记且怪物对象不为空，才可以遭遇
            if (this.hasMonster && this.monster != null) {
                return this.monster;
            }
            return null;
        }

        //接收怪物死亡/事件产生的掉落物（包含食物、工具、通关特殊材料）
        public void addDropItem(Item dropItem) {
            // 检验：掉落物不为空，才可以存入格子
            if (dropItem != null) {
                this.setResource(dropItem);
            }

        }

        //格子状态刷新预留方法（看着选用吧，不一定要写在这里）
//    public void refresh() {
//        Random random = new Random();
//
//        double monsterRate = 0.4;
//        double resourceRate = 0.5;
//
//        this.setMonster(null);
//        this.setResource(null);
//
//        if (random.nextDouble() < monsterRate) {
//            Monster newMonster = createMonsterByScene(sceneType, random);
//            this.setMonster(newMonster);
//        }
//
//        if (random.nextDouble() < resourceRate) {
//            Item newResource = createResourceByScene(sceneType, random);
//            this.setResource(newResource);
//        }
//    }
//
//    private Monster createMonsterByScene(String sceneType, Random random) {
//        switch (sceneType) {
//            case "沙滩":
//                return random.nextBoolean() ? new Crab() : null;
//            case "树林":
//                int rand = random.nextInt(3);
//                if (rand == 0) return new Monkey();
//                else if (rand == 1) return new BlueSheep();
//                else return null;
//            case "岩石区":
//                return random.nextBoolean() ? new BlueSheep() : null;
//            case "海边":
//                return random.nextBoolean() ? new TigerShark() : null;
//            default:
//                return null;
//        }
//    }
//
//    private Item createResourceByScene(String sceneType, Random random) {
//        switch (sceneType) {
//            case "沙滩":
//                int r1 = random.nextInt(3);
//                if (r1 == 0) return new Food("椰子", "食物", "解渴", "img/coconut.png", 1, "thirst", 15);
//                else if (r1 == 1) return new Food("贝壳", "食物", "恢复饥饿", "img/shell.png", 1, "hunger", 10);
//                else return new Tool("藤蔓", "材料", "基础材料", "img/vine.png", 1, 0, 0, 0);
//
//            case "树林":
//                int r2 = random.nextInt(4);
//                if (r2 == 0) return new Food("野果", "食物", "恢复饥饿", "img/fruit.png", 1, "hunger", 20);
//                else if (r2 == 1) return new Tool("木材", "材料", "基础材料", "img/wood.png", 1, 0, 0, 0);
//                else if (r2 == 2) return new Tool("树枝", "材料", "基础材料", "img/stick.png", 1, 0, 0, 0);
//                else return null;
//
//            case "岩石区":
//                int r3 = random.nextInt(3);
//                if (r3 == 0) return new Tool("矿石", "材料", "基础材料", "img/ore.png", 1, 0, 0, 0);
//                else if (r3 == 1) return new Tool("石头", "材料", "基础材料", "img/stone.png", 1, 0, 0, 0);
//                else return null;
//
//            case "海边":
//                int r4 = random.nextInt(3);
//                if (r4 == 0) return new Food("鲜鱼", "食物", "恢复饥饿", "img/fish.png", 1, "hunger", 30);
//                else if (r4 == 1) return new Food("海藻", "食物", "恢复口渴", "img/seaweed.png", 1, "thirst", 20);
//                else return null;
//
//            default:
//                return null;
//        }
//    }

}
