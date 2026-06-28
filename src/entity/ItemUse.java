package entity;
import entity.Item;
import entity.Player;

public class ItemUse {
    private static final ItemUse instance = new ItemUse();
    private ItemUse(){}
    public static ItemUse getInstance(){
        return instance;
    }

    // 1、使用食物（替代Food.use）
    public void eatFood(String foodName, Player player) {
        Item food = player.findItemByName(foodName);
        if(food == null || !"food".equals(food.getItemType())){
            System.out.println("该物品不是食物，无法食用");
            return;
        }
        if(food.getOwnCount() <= 0){
            System.out.println(food.getItemName() + "数量不足!");
            return;
        }
        if(player.getHp() <= 0){
            System.out.println("玩家已死亡，无法使用物品");
            return;
        }

        String type = food.getRecoverType();
        int val = food.getRecoverValue();
        switch (type){
            case "hunger":
                player.setHunger(Math.min(100, player.getHunger() + val));
                System.out.println("使用 " + food.getItemName() + "，饥饿值恢复 " + val);
                break;
            case "thirst":
                player.setThirst(Math.min(100, player.getThirst() + val));
                System.out.println("使用 " + food.getItemName() + "，口渴值恢复 " + val);
                break;
            case "fatigue":
                player.setFatigue(Math.min(100, player.getFatigue() + val));
                System.out.println("使用 " + food.getItemName() + "，疲惫值恢复 " + val);
                break;
            case "hp":
                player.setHp(Math.min(100, player.getHp() + val));
                System.out.println("使用 " + food.getItemName() + "，血量恢复 " + val);
                break;
            default:
                System.out.println("该食物无任何效果");
                return;
        }
        food.reduceCount(1);
        player.checkGameOver();
    }

    // 2、使用工具（替代Tool.use，移除采集加成）
    public void useTool(String toolName, Player player) {
        Item tool = player.findItemByName(toolName);
        if(tool == null || !"tool".equals(tool.getItemType())){
            System.out.println("该物品不是工具");
            return;
        }
        if(tool.getOwnCount() <= 0){
            System.out.println(tool.getItemName() + "数量不足，无法使用！");
            return;
        }
        int currDur = tool.getCurrentDurability();
        if(currDur <= 0){
            System.out.println(tool.getItemName() + "耐久度已耗尽，无法使用！");
            return;
        }

        System.out.println("使用【" + tool.getItemName() + "】，攻击+" + tool.getAttackBonus());
        tool.setCurrentDurability(currDur - 1);

        if(tool.getCurrentDurability() == 0){
            System.out.println(tool.getItemName() + "耐久耗尽，道具损毁！");
            tool.reduceCount(1);
        }else{
            System.out.println("剩余耐久度：" + tool.getCurrentDurability());
        }
    }

    /** 3、使用灯塔碎片建造（替代Clip.use，取消碎片编号）
    public void useLighthouseFragment(Player player) {
        Item fragment = player.findItemByName("灯塔碎片");
        if(fragment == null || fragment.getOwnCount() <= 0){
            System.out.println("没有灯塔碎片，无法建造灯塔！");
            return;
        }
        fragment.reduceCount(1);
        player.buildLighthouse();
        System.out.println("消耗1块灯塔碎片，灯塔建造进度提升");
        if(fragment.getOwnCount() <= 0){
            System.out.println("所获得的【灯塔碎片】已全部消耗");
        }
        player.checkGameOver();
    }*/

    // 4、材料提示（替代Material.use）
    public void useMaterial(String matName) {
        System.out.println(matName + " 是合成材料，不能直接使用");
    }
}