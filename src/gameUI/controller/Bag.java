package gameUI.controller;
import entity.Item;
import entity.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Bag {

    private gameUI.controller.Main main;
    private Player player;    // 只声明，不在这里初始化！
    private Item[] backpackArr;

    // 背包物品数量展示
    @FXML private Label coco;
    @FXML private Label fish;
    @FXML private Label pig;
    @FXML private Label rabbit;
    @FXML private Label mineral;
    @FXML private Label shell;
    @FXML private Label stone;
    @FXML private Label vine;
    @FXML private Label wood;
    @FXML private Label debris;
    @FXML private Label axe;
    @FXML private Label shell_axe;
    @FXML private Label hammer;
    @FXML private Label club;
    @FXML private Label drik;

    // ==============================
    // 【正确】FXML 加载完自动执行
    // 这里绝对不能调用 refreshUI()
    // ==============================
    @FXML
    public void initialize(){
        System.out.println("背包界面加载成功");
    }

    // ==============================
    // 【正确】主控制器调用这个方法
    // ==============================
    public void setMainController(Main main) {
        this.main = main;
        this.player = Player.getInstance();  // 在这里拿才安全！
        this.backpackArr = player.getBackpack();

        // 现在才安全刷新！
        refreshUI();
    }

    // ==============================
    // 【正确】刷新背包数量
    // 加了安全判断，不会崩溃
    // ==============================
    public void refreshUI() {
        if (backpackArr == null || backpackArr.length < 15) {
            System.out.println("背包数组为空或长度不够！");
            return;
        }

        coco.setText(String.valueOf(backpackArr[0].getOwnCount()));
        fish.setText(String.valueOf(backpackArr[1].getOwnCount()));
        pig.setText(String.valueOf(backpackArr[2].getOwnCount()));
        rabbit.setText(String.valueOf(backpackArr[3].getOwnCount()));
        mineral.setText(String.valueOf(backpackArr[4].getOwnCount()));
        shell.setText(String.valueOf(backpackArr[5].getOwnCount()));
        stone.setText(String.valueOf(backpackArr[6].getOwnCount()));
        vine.setText(String.valueOf(backpackArr[7].getOwnCount()));
        wood.setText(String.valueOf(backpackArr[8].getOwnCount()));
        debris.setText(String.valueOf(backpackArr[9].getOwnCount()));
        axe.setText(String.valueOf(backpackArr[10].getOwnCount()));
        shell_axe.setText(String.valueOf(backpackArr[11].getOwnCount()));
        hammer.setText(String.valueOf(backpackArr[12].getOwnCount()));
        club.setText(String.valueOf(backpackArr[13].getOwnCount()));
        drik.setText(String.valueOf(backpackArr[14].getOwnCount()));
    }
}