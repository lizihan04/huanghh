package entity;


public class Clip extends Item {
    // 碎片编号 1/2/3/4，四块碎片一一对应
    private int clipId;
    // 每块碎片提供20%灯塔建造进度
    private final int addProgress = 20;

    public Clip(String name, String type, String desc, String imgPath, int count, int clipId) {
        super(name, type, desc, imgPath, count);
        this.clipId = clipId;
    }

    // 使用碎片：调用玩家建造灯塔，消耗碎片
    @Override
    public void use(Player player) {
        // 碎片只能使用一次，数量扣减
        if (getCount() <= 0) {
            System.out.println("该碎片已被使用！");
            return;
        }
        // 使用碎片增加灯塔进度
        player.buildLighthouse();
        // 消耗1个碎片
        setCount(getCount() - 1);
        if (getCount() <= 0) {
            System.out.println("所获得的【" + getName() + "】已全部消耗");
        }
    }

    // 判断是否为同编号碎片
    public boolean equalsClip(Clip other) {
        if (other == null) return false;
        return this.clipId == other.getClipId();
    }

    // Getter
    public int getClipId() {
        return clipId;
    }

    public int getAddProgress() {
        return addProgress;
    }

    public void setClipId(int clipId) {
        this.clipId = clipId;
    }
}
