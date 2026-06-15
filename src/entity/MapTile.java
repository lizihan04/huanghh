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
                return temp2;
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
    public void refresh() {
//        Random random = new Random();
//        // 刷新概率：50%概率刷新怪物
//        double monsterRate = 0.5;
//        // 40%概率刷新资源
//        double resourceRate = 0.4;
//
//        // 先清空原有怪物、资源（每日刷新先重置）
//        this.setMonster(null);
//        this.setResource(null);
//
//        // 按地形刷新怪物
//        if (random.nextDouble() < monsterRate) {
//            String type = this.getSceneType();
//            Monster newMonster = null;
//            // 不同地形生成对应怪物
//            if ("树林".equals(type)) {
//                newMonster = new Hare("野兔", 30, 8, null, "img/hare.png");
//            } else if ("岩石区".equals(type)) {
//                newMonster = new Boar("血玉蜘蛛", 50, 12, null, "img/boar.png");
//            } else if ("海边".equals(type)) {
//                newMonster = new Snake("毒蛇", 45, 15, null, "img/snake.png");
//            } else if ("沙滩".equals(type)) {
//                newMonster = new Snake("螃蟹", 70, 20, null, "img/guard_snake.png");
//            }
//            // 沙滩默认不刷怪，newMonster 为 null 则不赋值
//            this.setMonster(newMonster);
//        }
//
//        // 按地形刷新资源（食物/材料）
//        if (random.nextDouble() < resourceRate) {
//            String type = this.getSceneType();
//            Item newResource = null;
//            if ("树林".equals(type)) {
//                newResource = new Food("野果", "食物", "恢复饥饿", "img/fruit.png", 2, "饥饿值", 20);
//            } else if ("岩石区".equals(type)) {
//                newResource = new Tool("矿石", "材料", "基础打造材料", "img/ore.png", 1, 0, 2, 0);
//            } else if ("海边".equals(type)) {
//                newResource = new Food("鲜鱼", "食物", "恢复饥饿", "img/fish.png", 1, "饥饿值", 30);
//            } else if ("沙滩".equals(type)) {
//                newResource = new Tool("椰子", "食物", "解渴", "img/.png", 1, 0, 0, 0);
//            }
//            this.setResource(newResource);
//        }
    }
}
