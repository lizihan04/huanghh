package entity;
import java.io.Serializable;

/**
 * 材料道具子类，继承抽象Item，实现use抽象方法，对应material类型物品
 */
public class Material extends Item implements Serializable {
    /**
     * 父类Item五参构造：name,type,effect,imgPath,count
     */
    public Material(String name, String type, String effect, String imgPath, int count) {
        super(name, type, effect, imgPath, count);
    }

    /**
     * 材料无法直接使用，重写抽象use方法
     */
    @Override
    public void use(Player player) {
        System.out.println(this.getName() + " 是合成材料，不能直接使用");
    }
}